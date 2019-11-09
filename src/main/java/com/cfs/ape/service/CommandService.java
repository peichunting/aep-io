package com.cfs.ape.service;

import com.cfs.ape.entity.AepCommand;

public interface CommandService {


    AepCommand saveCommand(AepCommand command);

    AepCommand updateCommand(AepCommand command);
}
