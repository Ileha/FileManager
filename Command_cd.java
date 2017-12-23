import com.company.*;
import java.util.ArrayList;

public class Command_cd extends ABSCommand {
    public Command_cd(Data _dir) {
        super("cd", 1, _dir);
    }

    @Override
    public void ABSExecCommand() throws FileNotFound {
        if (Commands.get(1).equals("..")) {
            if (!WorkDir.directory.current_directory.getPath().equals("/")) {
                WorkDir.directory.current_directory = WorkDir.directory.current_directory.getParentFile();
                WorkDir.Output.SetOutput("go to parent directory "+WorkDir.directory.current_directory.getName());
            }
            else  {
                throw new FileNotFound("Ошибка!!!", "/ is root");
            }
        }
        else {
            try {
                WorkDir.directory.current_directory = WorkDir.directory.GetFileOrDirectory(Commands.get(1), TypeOfFile.Directory);
                WorkDir.Output.SetOutput("go to children directory "+WorkDir.directory.current_directory.getName());
            } catch (FileNotFound err) {
                throw err;
            }
        }
    }

    @Override
    public String ForMan() {
        return "cd need 1 argument, change directory to path";
    }
}
