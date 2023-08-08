package com.cbt.authservicecbtaug23one;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyticsDatum
{
    String type;   // CREDENTIAL | USERDETAIL
    String message;
    String payload; // Actual Object Stringified to Json

}
