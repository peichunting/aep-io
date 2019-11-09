package com.cfs.ape.service;

import com.cfs.ape.entity.AepCommand;

public interface CommandHandleService {

    AepCommand handleCommand(AepCommand aepCommand);
}
