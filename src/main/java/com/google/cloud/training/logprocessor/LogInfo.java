/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.training.logprocessor;

import java.util.Date;

import org.joda.time.DateTime;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Sum;

import org.apache.avro.reflect.Nullable;
import com.google.gson.Gson;
import org.json.*;


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
        this.cpuMegaCycles  = cpuMegaCycles;
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
    
    
    public String toString() {
		return "recid " + logEntryId + " recLatency " + latency + " " + nickname + " " + httpMethod + " " + cost + " " + cpuMegaCycles + " " + httpStatusCode;
	}

    
    public static LogInfo Parse(String entry) {
		try {
			LogInfo logMessage = new LogInfo();
			
			JSONObject obj = new JSONObject(entry);
			
			logMessage.logEntryId = obj.getString("insertId");
			logMessage.httpStatusCode = obj.getJSONObject("httpRequest").getInt("status");
			
			String latency = obj.getJSONObject("protoPayload").getString("latency");
				if (latency != null && latency.length() > 0 && latency.charAt(latency.length() - 1) == 's') {
        			latency = latency.substring(0, latency.length() - 1);
    			}	
    		logMessage.latency = Double.valueOf(latency);
    		
    		
			logMessage.cpuMegaCycles = obj.getJSONObject("protoPayload").getDouble("megaCycles");
			logMessage.cost = obj.getJSONObject("protoPayload").getDouble("cost");
			logMessage.nickname = obj.getJSONObject("protoPayload").getString("nickname");
			logMessage.httpMethod = obj.getJSONObject("protoPayload").getString("method");
			

		
			return logMessage;

		} catch (Exception e) {
			return null; // Return null if any error parsing.
		}
	}
	
		
		static class ParseLines extends DoFn<String, String> {
		
			@ProcessElement
			public void processElement(ProcessContext c) {
				String logLine = c.element();
				LogInfo info = LogInfo.Parse(logLine);
				if (info == null) {
					//
				} else {
					c.output(info.toString());
				}
			}
		}
		
    
}
