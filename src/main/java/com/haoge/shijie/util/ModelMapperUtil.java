package com.haoge.shijie.util;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperUtil {

    public static ModelMapper get() {
        return new ModelMapper();
    }

    /**
     * 严格匹配模式
     * @return modelMapper
     */
    public static ModelMapper getStrictModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

}
