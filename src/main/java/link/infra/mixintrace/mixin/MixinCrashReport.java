package link.infra.mixintrace.mixin;

import link.infra.mixintrace.TraceUtils;
import net.minecraft.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReport.class)
public abstract class MixinCrashReport {
	@Shadow private StackTraceElement[] uncategorizedStackTrace;

	@Inject(method = "getDetails(Ljava/lang/StringBuilder;)V", at = @At("TAIL"))
	private void mixintrace_addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
		int trailingNewlineCount = 0;
		// Remove trailing \n
		while (crashReportBuilder.length() > 0 && trailingNewlineCount < 2
				&& crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
			crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
			trailingNewlineCount++;
		}
		TraceUtils.printTrace(uncategorizedStackTrace, crashReportBuilder);
		crashReportBuilder.append("\n".repeat(trailingNewlineCount));
	}
}
