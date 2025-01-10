/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alinesno.infra.business.platform.install.shell.domain;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoxiaodong
 */
@SuppressWarnings("serial")
public class CmdResult extends Jsonable {

    public static final Integer EXIT_VALUE_FOR_KILL = 143; // auto set while kill process

    public static final Integer EXIT_VALUE_FOR_REJECT = -100;

    public static final Integer EXIT_VALUE_FOR_TIMEOUT = -200;

    public static final CmdResult EMPTY = new CmdResult();

    /**
     * Only agent local, cannot parse to json
     */
    private transient Process process;

    /**
     * Related cmd id
     */
    private String cmdId;

    /**
     * Process id
     */
    private Integer processId;

    /**
     * Linux exit status code, -100 means killed
     */
    private Integer exitValue;

    /**
     * Cmd running duration in second
     */
    private Long duration;

    /**
     * Cmd duration with logging in second
     */
    private Long totalDuration;

    /**
     * Cmd start time
     */
    private ZonedDateTime startTime;

    /**
     * Cmd executed time
     */
    private ZonedDateTime executedTime;

    /**
     * Cmd finish time with logging
     */
    private ZonedDateTime finishTime;

    /**
     * Env for output
     */
    private Map<String, String> output = new HashMap<>(5);

    /**
     * Exception while cmd running
     */
    private List<Throwable> exceptions = new ArrayList<>(5);

    public CmdResult(Integer exitValue) {
        this.exitValue = exitValue;
    }

    public CmdResult() {
		// TODO Auto-generated constructor stub
	}

	public void setExecutedTime(ZonedDateTime executedTime) {
        if (executedTime != null) {
            this.executedTime = executedTime;
            if (startTime != null) {
                this.duration = ChronoUnit.SECONDS.between(this.startTime, this.executedTime);
                this.totalDuration = this.duration;
            }
        }
    }

    public void setFinishTime(ZonedDateTime finishTime) {
        if (finishTime != null) {
            this.finishTime = finishTime;
            if (this.startTime != null) {
                this.totalDuration = ChronoUnit.SECONDS.between(this.startTime, this.finishTime);
            }
        }
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getExitValue() {
		return exitValue;
	}

	public void setExitValue(Integer exitValue) {
		this.exitValue = exitValue;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public Map<String, String> getOutput() {
		return output;
	}

	public void setOutput(Map<String, String> output) {
		this.output = output;
	}

	public static Integer getExitValueForKill() {
		return EXIT_VALUE_FOR_KILL;
	}

	public static Integer getExitValueForReject() {
		return EXIT_VALUE_FOR_REJECT;
	}

	public static Integer getExitValueForTimeout() {
		return EXIT_VALUE_FOR_TIMEOUT;
	}

	public static CmdResult getEmpty() {
		return EMPTY;
	}

	public ZonedDateTime getExecutedTime() {
		return executedTime;
	}

	public ZonedDateTime getFinishTime() {
		return finishTime;
	}

	public void setExceptions(List<Throwable> exceptions) {
		this.exceptions = exceptions;
	}

	@Override
    public String toString() {
        return "CmdResult{" +
            "processId=" + processId +
            ", exitValue=" + exitValue +
            ", duration=" + duration +
            ", totalDuration=" + totalDuration +
            ", startTime=" + startTime +
            ", executedTime=" + executedTime +
            ", finishTime=" + finishTime +
            ", outputSize=" + output.size() +
            '}';
    }
}
