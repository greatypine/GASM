package com.cnpc.pms.base.init.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import com.cnpc.pms.base.init.model.PMSInit;
import com.cnpc.pms.base.util.StrUtil;

public class AutoGenerateContext extends AbstractContext {

	private int target;

	public int getTarget() {
		return target;
	}

	public AutoGenerateContext(PMSInit init) {
		createFromPMSInit(init);

		target = init.getTarget();

		String resourceLocation = init.getResource();
		if (StrUtil.isBlank(resourceLocation) == true) {
			error = true;
			log.error("Fail to find the script:{}", init.getDescription());
		} else {
			InitResource resource = new InitResource(resourceLocation);
			script = resource.getScript();
			contextLog = resource.getContentLog();
		}
	}

	/**
	 * Used for AutoGenerateContext & InitContext
	 * 
	 * @param init
	 */
	protected void createFromPMSInit(PMSInit init) {
		description = init.getDescription();
		batchSize = init.getBatchSize() > 0 ? init.getBatchSize() : BATCH_SIZE;
		injectBeans = init.getInjectBeans();
		commitImmediately = init.isCommitImmediately();
	}

	/**
	 * Used for AutoGenerateContext
	 * 
	 * @param init
	 */
	@Override
	protected void addSpecialJSObject() {
		Object wrappedOut = Context.javaToJS(log, shell);
		ScriptableObject.putProperty(shell, "log", wrappedOut);
		RandomUtil util = new RandomUtil();
		Object wrappedUtil = Context.javaToJS(util, shell);
		ScriptableObject.putProperty(shell, "U", wrappedUtil);

	}

	public AutoGenerateContext cloneScript() {
		AutoGenerateContext context = new AutoGenerateContext();
		copyProperties(context);
		context.contextLog = this.contextLog;
		context.target = this.target;
		return context;
	}

	protected AutoGenerateContext() {
	}

	/**
	 * Used for AutoGenerateContext & InitContext
	 * 
	 * @param init
	 */
	protected void copyProperties(AbstractContext context) {
		context.description = this.description;
		context.batchSize = this.batchSize;
		context.injectBeans = this.injectBeans;
		context.commitImmediately = this.isCommitImmediately();
		context.script = this.script;
	}

}
