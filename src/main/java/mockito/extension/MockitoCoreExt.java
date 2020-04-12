package mockito.extension;

import org.mockito.MockingDetails;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.listeners.VerificationStartedNotifier;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.invocation.MockHandler;
import org.mockito.verification.VerificationMode;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.internal.exceptions.Reporter.*;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.MockUtil.getMockHandler;

public class MockitoCoreExt extends MockitoCore {
    private Map<Object, VerificationMode> mockMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            throw nullPassedToVerify();
        }
        MockingDetails mockingDetails = mockingDetails(mock);
        if (!mockingDetails.isMock()) {
            throw notAMockPassedToVerify(mock.getClass());
        }
        assertNotStubOnlyMock(mock);
        MockHandler handler = mockingDetails.getMockHandler();
        mock = (T) VerificationStartedNotifier.notifyVerificationStarted(
                handler.getMockSettings().getVerificationStartedListeners(), mockingDetails);

        MockingProgress mockingProgress = mockingProgress();
        VerificationMode verificationMode;
        if (mockMap.containsKey(mock)) {
            verificationMode = mockMap.get(mock);
        } else {
            VerificationMode actualMode = mockingProgress.maybeVerifyLazily(mode);
            verificationMode = new MockAwareVerificationModeExt(mock, actualMode, mockingProgress.verificationListeners());
            mockMap.put(mock, verificationMode);
        }
        mockingProgress.verificationStarted(verificationMode);
        return mock;
    }

    private void assertNotStubOnlyMock(Object mock) {
        if (getMockHandler(mock).getMockSettings().isStubOnly()) {
            throw stubPassedToVerify(mock);
        }
    }
}
