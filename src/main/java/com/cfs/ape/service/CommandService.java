package com.cfs.ape.service;

import com.cfs.ape.entity.AepCommand;

import java.util.List;

public interface CommandService {


    AepCommand saveCommand(AepCommand command);

    AepCommand updateCommand(AepCommand command);

    List<AepCommand> listPreparedOrRetryCommand();
}
