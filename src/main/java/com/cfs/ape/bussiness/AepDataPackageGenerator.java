package com.cfs.ape.bussiness;

import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.util.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AepDataPackageGenerator implements DataPackageGenerator {

    private static final byte START_CHAR = 64;

    private static final byte END_CHAR = 35;

    private static final int INSTRUCTION_LENGTH = 22;

    @Value("${aep.app.main.version}")
    private int mainVersion;

    @Value("${aep.app.customer.version}")
    private int customerVersion;

    @Autowired
    private RedissonUtil redissonUtil;

    @Override
    public byte[] generateDataPackage(AepCommand aepCommand) {
        byte[] instruction = new byte[22];

        instruction[0] = START_CHAR;
        instruction[1] = START_CHAR;
        StringBuilder keyBuilder = new StringBuilder();
        String productId = aepCommand.getProductId();
        productId = StringUtils.isBlank(productId) ? "" : productId;
        keyBuilder.append(productId);
        String deviceGroupId = aepCommand.getDeviceGroupId();
        deviceGroupId = StringUtils.isBlank(deviceGroupId) ? "" : deviceGroupId;
        keyBuilder.append(":").append(deviceGroupId);
        String deviceId = aepCommand.getDeviceId();
        deviceId = StringUtils.isBlank(deviceId) ? "" : deviceId;
        keyBuilder.append(":").append(deviceId);

        //设置业务流水号字节
        try {
            long bussinessId = redissonUtil.getAtomicLong(keyBuilder.toString());
            long lowerByte = bussinessId & 0xff;
            long upperByte = bussinessId >> 8;
            instruction[2] = (byte)lowerByte;
            instruction[3] = (byte)upperByte;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String imei = aepCommand.getImei();
        String lastNineChar = imei;
        if(StringUtils.isNotBlank(imei)){
            if(imei.length() >= 9) {
                lastNineChar = imei.substring(imei.length() - 9);
            }
        }else{
            lastNineChar = "0";
        }

        //设置版本号字节
        instruction[4] = (byte)mainVersion;
        instruction[5] = (byte)customerVersion;

        //设置信息体字节
        String argsStr = aepCommand.getArgs();
        int[] argsInt = null;
        if(StringUtils.isNotBlank(argsStr)){
            String[] args = argsStr.split(",");
            if(args.length >= 1){
                argsInt = new int[args.length];
                for (int i = 0 ;i < args.length ; i++) {
                    argsInt[i] = Integer.valueOf(args[i]);
                }


            }
        }
        if(getInfoBytes(aepCommand.getCommandType(),instruction,argsInt) == null){
            return null;
        }

        //设置源地址字节
        int imeiInt = Integer.valueOf(lastNineChar);
        instruction[12] = (byte)(imeiInt & 0xff);
        instruction[13] = (byte)((imeiInt >> 8) & 0xff);
        instruction[14] = (byte)((imeiInt >> 16) & 0xff);
        instruction[15] = (byte)((imeiInt >> 24) & 0xff);
        instruction[16] = (byte)(aepCommand.getDeviceType());
        instruction[17] = (byte)(aepCommand.getModel());

        //设置命令字节
        instruction[18] = (byte)(aepCommand.getCommandType());
        //计算校验和
        int sum = 0;
        for(int i = 2; i < 19;i++){
            sum += (int)instruction[i];
        }
        sum = sum & 0xff;
        instruction[19] = (byte)sum;
        instruction[20] = END_CHAR;
        instruction[21] = END_CHAR;
        return instruction;
    }


    //private int

    public byte[] getInfoBytes(int commandType,byte[] instruction,int ... args){
        switch (commandType){
            case 112:
                if(args == null || args.length < 1){
                    return null;
                }
                int clearType = args[0];
                if(clearType >= 128 ){
                    clearType = clearType - 256;
                }
                instruction[6] = (byte)clearType;
                for(int i = 7 ; i < 12 ; i++){
                    instruction[i] = 0;
                }
                return instruction;
            case 100:
                for(int i = 6 ; i < 12 ; i++){
                    instruction[i] = 0;
                }
                return instruction;
            case 101:
                instruction[6] = 0;
                instruction[7] = 0;
                if(args == null || args.length < 1){
                    return null;
                }

                instruction[8] = (byte)args[0];
                instruction[9] = 0;
                instruction[10] = 0;
                instruction[11] = 0;
                return instruction;
            case 102:
                for(int i = 6 ; i < 10 ; i++){
                    instruction[i] = 0;
                }
                if(args == null || args.length < 1){
                    return null;
                }
                instruction[10] = (byte)args[0];
                instruction[11] = 0;
                return instruction;
            default:
                if(args == null || args.length < 6){
                    return null;
                }
                int signalStrength = args[0];
                int smokeDense = args[1];
                int alertAgile = args[2];
                int batteryVolt = args[3];
                int heartBeat = args[4];
                int temperature = args[5];
                instruction[6] = (byte)signalStrength;
                instruction[7] = (byte)smokeDense;
                instruction[8] = (byte)alertAgile;
                instruction[9] = (byte)batteryVolt;
                instruction[10] = (byte)heartBeat;
                instruction[11] =(byte)temperature;
                return instruction;

        }
    }
}
