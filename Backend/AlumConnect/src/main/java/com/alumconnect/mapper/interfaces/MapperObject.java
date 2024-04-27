package com.alumconnect.mapper.interfaces;

/**
 * Mapper object to map two beans.
 */
public interface MapperObject <I, O>{
    O map(I input);

}
