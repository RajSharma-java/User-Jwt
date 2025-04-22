package com.user.Common;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Helper
{
    @Bean
    public ModelMapper mapper(){
        return  new ModelMapper();
    }
}
