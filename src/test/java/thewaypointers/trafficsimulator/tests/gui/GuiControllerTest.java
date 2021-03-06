package thewaypointers.trafficsimulator.tests.gui;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import thewaypointers.trafficsimulator.common.ISimulationInputListener;
import thewaypointers.trafficsimulator.gui.GuiController;

import java.util.ArrayList;
import java.util.List;

/* Sample test class.
   Name has to end with Test so Maven finds it.
 */
public class GuiControllerTest {

    public class MockSimulationInputListener implements ISimulationInputListener{

        public List<String> parameterChangeEvents = new ArrayList<>();

        @Override
        public void SimulationParameterChanged(String parameterName, String value) {
            parameterChangeEvents.add(parameterName);
        }
    }

    /*  Sample test.
        It's best to use very descriptive names for the tests,
        so that when we run them, we can instantly know what exactly
        passes and what fails.
     */
    @Test
    public void GuiController_notifies_listener_about_number_of_vehicles_change(){

        // arrange
        MockSimulationInputListener listener = new MockSimulationInputListener();
        GuiController guiController = new GuiController(listener);

        // act
        guiController.numberOfVehiclesChanged(15);

        // assert
        assertThat(listener.parameterChangeEvents).contains("numberOfVehicles");
    }
}
