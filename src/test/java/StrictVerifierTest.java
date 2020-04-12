import mockito.extension.StrictVerifier;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class StrictVerifierTest {
    public static class Target {
        Integer num;

        public void setNum(Integer num) {
            this.num = num;
        }

        public void setNum2(Integer num, String a) {
            this.num = num;
            System.out.println(a);
        }

        public void never(Integer num, String a) {
            throw new RuntimeException("expected never called");
        }
    }

    @Test
    public void verifySample2() {
        StrictVerifier strictVerifier = new StrictVerifier();
        Target mock = mock(Target.class);
        mock.setNum(1);
        mock.setNum(1);
//        mock.setNum(2);
        strictVerifier.verify(mock, times(2)).setNum(1);

        mock.setNum2(1, "b");
        strictVerifier.verify(mock, times(1)).setNum2(1, "b");
    }
}
