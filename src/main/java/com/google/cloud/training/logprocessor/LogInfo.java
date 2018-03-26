/**
Copyright Google Inc. 2015
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/

package com.google.cloud.solutions;

import java.util.Date;

import org.joda.time.DateTime;

import com.google.cloud.dataflow.sdk.coders.AvroCoder;
import com.google.cloud.dataflow.sdk.coders.DefaultCoder;
import com.google.cloud.dataflow.sdk.transforms.Aggregator;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.Sum;

import org.apache.avro.reflect.Nullable;


@DefaultCoder(AvroCoder.class)
class LogInfo {
    private String logEntryId ;
    @Nullable private int httpStatusCode;
    @Nullable private double latency;
    @Nullable private double cpuMegaCycles;
    @Nullable private double cost;
    @Nullable private String nickname;
    @Nullable private String httpMethod;


    @SuppressWarnings("unused")
    public LogInfo() {}

    public LogInfo(String logEntryId, int httpStatusCode, double latency, double cpuMegaCycles, double cost,
                      String nickname, String httpMethod) {
        this.logEntryId = logEntryId;
        this.httpStatusCode  = httpStatusCode;
        this.latency  = latency;
        this.cpuMegaCycles  = cpuMegaCycle;
        this.cost  = cost;
        this.nickname  = nickname;
        this.httpMethod  = httpMethod;
    
    }

    public String getLogEntryId() {
        return this.logEntryId;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public double getLatency() {
        return this.latency;
    }

    public double getCpuMegaCycles() {
        return this.cpuMegaCycles;
    }
    
    public double getCost() {
        return this.cost;
    }
    
    
    public String getNickname() {
        return this.nickname;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

}
