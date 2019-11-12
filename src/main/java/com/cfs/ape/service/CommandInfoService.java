package com.cfs.ape.service;

import com.cfs.ape.entity.AepCommandInfo;

public interface CommandInfoService {

    AepCommandInfo getCommandInfoByCommandType(int commandType);

    AepCommandInfo updateAepCommandInfo(AepCommandInfo aepCommandInfo);

    AepCommandInfo saveAepCommandInfo(AepCommandInfo aepCommandInfo);
}
