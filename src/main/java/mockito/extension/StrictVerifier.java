package mockito.extension;

import org.mockito.verification.VerificationMode;

import java.util.HashMap;
import java.util.Map;

public class StrictVerifier {
    Map<Object, Object> mockMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T verify(T mock, VerificationMode mode) {
//        if (mockMap.containsKey(mock)) {
//            return (T) mockMap.get(mock);
//        }
//        T verify = new MockitoCoreExt().verify(mock, mode);
//        mockMap.put(mock, verify);
//        return verify;
        return new MockitoCoreExt().verify(mock, mode);
    }
}
