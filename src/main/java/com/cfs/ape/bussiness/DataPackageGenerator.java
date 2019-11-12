package com.cfs.ape.bussiness;

import com.cfs.ape.entity.AepCommand;

public interface DataPackageGenerator {

    public byte[] generateDataPackage(AepCommand aepCommand);
}
