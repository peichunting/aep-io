package com.cfs.ape.bussiness;

import com.cfs.ape.entity.AepCommand;

public interface DataPackageGenerator {

    byte[] generateDataPackage(AepCommand aepCommand);
}
