import Controller.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    public void testGetController() {
        Controller controller1 = Controller.getController();
        Controller controller2 = Controller.getController();

        assertSame(controller1, controller2);
    }
}