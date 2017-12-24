import com.company.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Command_nano extends ABSCommand {
    public Command_nano(Data _dir) {
        super("nano", 1, _dir);
    }

    public void ABSExecCommand() throws FileNotFound, IOException {
        File f = null;
        try {
            f = WorkDir.directory.GetFileOrDirectory(Commands.get(1),TypeOfFile.File);
        } catch (FileNotFound fileNotFound) {
            WorkDir.Output.SetOutput(fileNotFound.toString()+"\n Create file? y/n");
            if (WorkDir.Input.GetInput().equals("y")) {
                ArrayList<String> str = new ArrayList<String>();
                str.add("touch"); str.add(Commands.get(1));
                WorkDir.command_shifre(str);
                WorkDir.command_shifre(Commands);
            }
            return;
        }

        ProcessBuilder procBuilder = new ProcessBuilder("open", f.getAbsolutePath());
        try {
            Process process = procBuilder.start();
        } catch (IOException e) {
            throw e;
        }
        WorkDir.Output.SetOutput("opening file "+Commands.get(1));
    }

    public String ForMan() {
        return "nano need 1 argument, open or/and create file";
    }
}
