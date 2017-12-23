import com.company.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Command_help extends ABSCommand {
    public Command_help(Data _dir) {
        super("help", 0, _dir);
    }

    @Override
    public void ABSExecCommand() {
        if (Commands.size() == 1) {
            WorkDir.Output.SetOutput("This is man\nPrint help and command which interests you\nAvalible commands");
            for (ABSCommand com : WorkDir.exec_commands) {
                WorkDir.Output.SetOutput(com.GetName());
            }
        }
        else {
            try {
                ABSCommand ex = WorkDir.FindCommandByName(Commands.get(1));
                WorkDir.Output.SetOutput("This is man by "+ex.GetName()+"\n"+ex.ForMan());
            }
            catch (NoSuchElementException err) {
                WorkDir.Output.SetOutput("command "+Commands.get(0)+" not found");
            }
        }
    }

    @Override
    public String ForMan() {
        return "help need 0 or 1 argument, print help or man about command";
    }
}
