package mockito.extension;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockAwareVerificationModeExt extends MockAwareVerificationMode {
    public MockAwareVerificationModeExt(Object mock, VerificationMode mode, Set<VerificationListener> listeners) {
        super(mock, mode, listeners);
    }

    @Override
    public void verify(VerificationData data) {
        super.verify(data);

        // additional verify for any args
        MatchableInvocation target = data.getTarget();
        List<ArgumentMatcher> anyMatchers = IntStream.range(0, target.getMatchers().size()).mapToObj(i -> Any.ANY).collect(Collectors.toList());
        InvocationMatcher anyUnvocationMatcher = new InvocationMatcher(target.getInvocation(), anyMatchers);
        VerificationData anyVerificationData = new VerificationData() {
            @Override
            public List<Invocation> getAllInvocations() {
                return data.getAllInvocations();
            }

            @Override
            public MatchableInvocation getTarget() {
                return anyUnvocationMatcher;
            }

            @Override
            public InvocationMatcher getWanted() {
                return anyUnvocationMatcher;
            }
        };

        super.verify(anyVerificationData);
    }
}