import com.company.*;
import java.util.ArrayList;

public class Command_pwd extends ABSCommand {
    public Command_pwd(Data _dir) {
        super("pwd", 0,_dir);
    }

    @Override
    public void ABSExecCommand() {
        WorkDir.Output.SetOutput(WorkDir.directory.current_directory.getPath());
    }

    public String ForMan() {
        return "pwd need 0 argument, print current directory";
    }
}
