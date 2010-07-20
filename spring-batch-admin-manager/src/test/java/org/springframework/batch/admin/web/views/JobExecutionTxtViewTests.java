/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.admin.web.views;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.admin.web.JobExecutionInfo;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.WebApplicationContextLoader;
import org.springframework.web.servlet.View;

@ContextConfiguration(loader = WebApplicationContextLoader.class, inheritLocations = false, locations = "AbstractManagerViewTests-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JobExecutionTxtViewTests extends AbstractManagerViewTests {

	private final HashMap<String, Object> model = new HashMap<String, Object>();

	@Autowired
	@Qualifier("jobs/execution.txt")
	private View view;

	@Test
	public void testLaunchViewWithJobExecution() throws Exception {
		model.put("jobExecution", MetaDataInstanceFactory.createJobExecutionWithStepExecutions(123L, Arrays.asList(
				"foo", "bar")));
		model.put("baseUrl", "http://localhost:8080/springsource");
		view.render(model, request, response);
		String content = response.getContentAsString();
		// System.err.println(content);
		assertTrue(content.contains("Job Execution"));
		assertTrue(content.contains("Job Execution: link="));
		assertTrue(content.contains("  status=STARTING"));
		assertTrue(content
				.contains("Job Instance: link=http://localhost:8080/springsource/batch/jobs/job/12/executions"));
	}

	@Test
	public void testLaunchViewWithJobExecutionInfo() throws Exception {
		JobExecution jobExecution = MetaDataInstanceFactory.createJobExecutionWithStepExecutions(123L, Arrays.asList(
				"foo", "bar"));
		model.put("jobExecution", jobExecution);
		model.put("jobExecutionInfo", new JobExecutionInfo(jobExecution, TimeZone.getDefault()));
		model.put("baseUrl", "http://localhost:8080/springsource");
		view.render(model, request, response);
		String content = response.getContentAsString();
		// System.err.println(content);
		assertTrue(content.contains("  duration=-"));
	}

}
