package com.alinesno.cloud.busines.platform.install.service.impl;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alinesno.cloud.busines.platform.install.shell.domain.CmdResult;
import com.alinesno.cloud.busines.platform.install.shell.runner.CmdExecutor;
import com.alinesno.cloud.busines.platform.install.shell.runner.Log;
import com.alinesno.cloud.busines.platform.install.shell.runner.LogListener;
import com.alinesno.cloud.busines.platform.install.shell.runner.ProcListener;
import com.google.common.collect.Lists;

class RunInstallServiceTest {

	@Test
	void testInstallAcp() {
		fail("Not yet implemented");
	}

	@Test
	void testInstallAcpByKubernetes() {
		fail("Not yet implemented");
	}

	@Test
	void testInstallAcpByDockerCompose() {
		fail("Not yet implemented");
	}

	@Test
	void testGetInstallFile() {
		fail("Not yet implemented");
	}

	@Test
	void testGetRunnerLog() {
		fail("Not yet implemented");
	}

	@Test
	void testGetInstallStatus() {
		fail("Not yet implemented");
	}
	
	private class NullProcListener implements ProcListener {

        @Override
        public void onStarted(CmdResult result) {
        	System.out.println("---> onStarted ,  result = " + result.toJson());
        }

        @Override
        public void onLogged(CmdResult result) {
        	System.out.println("---> onLogged ,  result = " + result.toJson());
        }

        @Override
        public void onExecuted(CmdResult result) {
        	System.out.println("---> onExecuted ,  result = " + result.toJson());
        }

        @Override
        public void onException(CmdResult result) {
        	System.out.println("---> onException ,  result = " + result.toJson());
        }
    }
	
	private LogListener logListener = new LogListener() {
	
		private boolean isFinish = false ; 
		private String cmdLogContent = null ;
		
		@Override
		public void onLog(Log log) {
			System.out.println(log);
			cmdLogContent = log.getContent() ;
			isFinish = false ; 
		}

		@Override
		public void onFinish() {
			System.out.println("读写完成.");
			isFinish = true ; 
		}

		public boolean isFinish() {
			return isFinish;
		}

		@Override
		public String cmdLogContent() {
			return cmdLogContent ;
		}
		
	};

	@Test
	public void testCmdExecutorStringListOfString() {
	
		List<String> shellList = new ArrayList<String>() ; 
		
		String cmd1 = "/usr/local/bin/docker run -d -p 8083:8080 tomcat:9" ; 
		String cmd2 = "/usr/local/bin/docker run -d -p 8082:8080 tomcat:7" ; 
	
		shellList.add(cmd1) ; 
		shellList.add(cmd2) ; 
		
		CmdExecutor executor = new CmdExecutor(new NullProcListener() , logListener, null, null, Lists.newArrayList("CMD_RUNNER_TEST"),null, shellList);
		CmdResult result = executor.run();
		
		System.out.println(result);
		System.out.println("logListener = " + logListener.isFinish() + ", content = " + logListener.cmdLogContent());
	}

}
