import com.company.*;
import java.io.File;

public class Command_rm extends ABSCommand {
    public Command_rm(Data _dir) {
        super("rm", 1, _dir);
    }

    public void ABSExecCommand() throws FileNotFound {
        File f = WorkDir.directory.GetFileOrDirectory(Commands.get(1), TypeOfFile.All);
        WorkDir.Output.SetOutput("deleting item by path: "+f.getAbsolutePath());
        WorkDir.Output.SetOutput("is "+f.delete());
    }

    public String ForMan() {
        return "rm need 1 argument, delete file or directory";
    }
}
