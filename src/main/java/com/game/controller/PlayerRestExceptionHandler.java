package com.game.controller;

import com.game.entity.exception.PlayerException;
import com.game.entity.exception.PlayerExceptionEntity;
import com.game.entity.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlayerRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PlayerExceptionEntity> handleException(PlayerException ex) {

        PlayerExceptionEntity playerExceptionEntity = new PlayerExceptionEntity();

        if(ex instanceof PlayerNotFoundException) {
            playerExceptionEntity.setReason("Player with this ID not found.");
            return new ResponseEntity<>(playerExceptionEntity ,HttpStatus.NOT_FOUND);
        }
        playerExceptionEntity.setReason("Not Valid ID.");
        return new ResponseEntity<PlayerExceptionEntity>(playerExceptionEntity, HttpStatus.BAD_REQUEST);
    }

}
