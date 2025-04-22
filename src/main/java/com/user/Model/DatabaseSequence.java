package com.user.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class DatabaseSequence
{
    @Id
    private String id;
    private  long seq;
}
