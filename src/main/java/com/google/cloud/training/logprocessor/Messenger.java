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

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

/**
 * A dataflow pipeline that prints the lines that match a specific search term
 * 
 * @author ajaych
 *
 */
public class Messenger {

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();
		Pipeline p = Pipeline.create(options);

		String input = "src/main/java/com/google/cloud/training/logprocessor/samplelog.json";
		String outputPrefix = "/tmp/output";
		final String searchTerm = "error";

		p //
				.apply("GetLogs", TextIO.read().from(input)) //
				.apply("Extract", ParDo.of(new DoFn<String, String>() {
				 @ProcessElement
          public void processElement(ProcessContext c) {
          String entry = c.element();

          JsonParser parser = new JsonParser();
          JsonElement element = parser.parse(entry);
            if (element.isJsonNull()) {
              return;
            }
            JsonObject root = element.getAsJsonObject();
            JsonArray lines = root.get("protoPayload").getAsJsonObject().get("line").getAsJsonArray();
            for (int i = 0; i < lines.size(); i++) {
              JsonObject line = lines.get(i).getAsJsonObject();
              String logMessage = line.get("logMessage").getAsString();

              // Do what you need with the logMessage here
              c.output(logMessage);
             }
          }
				})) //
				.apply(TextIO.write().to(outputPrefix).withSuffix(".txt").withoutSharding());

		p.run();
	}
}