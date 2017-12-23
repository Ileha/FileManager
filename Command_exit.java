import com.company.*;

public class Command_exit extends ABSCommand {
    public Command_exit(Data _dir) {
        super("exit", 0, _dir);
    }

    @Override
    public void ABSExecCommand() throws FileNotFound {
        WorkDir.execute = false;
        WorkDir.Output.SetOutput("exit from seans");
    }

    public String ForMan() {
        return "exit need 0 argument, end session";
    }
}
