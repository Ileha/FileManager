import com.company.*;

import java.io.File;
import java.io.IOException;

public class Command_touch extends ABSCommand {
    public Command_touch(Data _dir) {
        super("touch", 1, _dir);
    }

    public void ABSExecCommand() throws FileNotFound, ExeptionOnCommand {
        File f = null;
        String s;
        try {
            s = GetPath(Commands.get(1));
            try {
                f = WorkDir.directory.GetFileOrDirectory(s, TypeOfFile.Directory);
            } catch (FileNotFound fileNotFound) {
                throw fileNotFound;
            }
        }
        catch (FileNotFound err) {
            f = WorkDir.directory.current_directory;
        }

        WorkDir.Output.SetOutput("Creating file: "+f.getAbsolutePath()+"/"+GetFile(Commands.get(1)));
        f = new File(f.getAbsolutePath()+"/"+GetFile(Commands.get(1)));
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new ExeptionOnCommand(GetName());
        }
    }

    public String ForMan() {
        return "touch need 1 argument, and create file";
    }
}
