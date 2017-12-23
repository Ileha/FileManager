import com.company.*;
import java.io.File;

public class Command_mkdir extends ABSCommand {
    public Command_mkdir(Data _dir) {
        super("mkdir", 1, _dir);
    }

    public void ABSExecCommand() throws FileNotFound, ExeptionOnCommand {

        String new_dir = "";
        File f = null;
        try {new_dir = GetFile(Commands.get(1));}
        catch (FileNotFound err) {throw new ExeptionOnCommand(GetName());}

        try {f = WorkDir.directory.GetFileOrDirectory(GetPath(Commands.get(1)),TypeOfFile.Directory);}
        catch (FileNotFound err) {f = WorkDir.directory.GetFileOrDirectory("",TypeOfFile.Directory);}

        f = new File(f.getPath()+"/"+new_dir);
        if (f.mkdir()) {WorkDir.Output.SetOutput("каталог "+new_dir+" успешно создан");}
        else {throw new ExeptionOnCommand(GetName());}
    }

    public String ForMan() {
        return "mkdir need 1 argument, create directory";
    }
}
