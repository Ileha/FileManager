import com.company.*;
import java.io.File;

public class Command_test extends ABSCommand {
    public Command_test(Data _dir) {
        super("test", 0, _dir);
    }

    public void ABSExecCommand() throws FileNotFound {
        WorkDir.Output.SetOutput("enter your name");
        WorkDir.Output.SetOutput("Hello "+WorkDir.Input.GetInput());
    }

    public String ForMan() {
        return "test need 1 argument, Greets you";
    }
}
