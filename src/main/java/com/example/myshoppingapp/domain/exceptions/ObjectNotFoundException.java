package com.example.myshoppingapp.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Object was not found.")
public class ObjectNotFoundException extends RuntimeException {
  private final long objectId;


  public ObjectNotFoundException(Long objectId) {

    super("Object with ID " + objectId +  " not found!");

    this.objectId = objectId;
  }

  public long getObjectId() {
    return objectId;
  }


}
