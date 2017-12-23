import com.company.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Command_ls extends ABSCommand {
    public Command_ls(Data _dir) {
        super("ls", 0,_dir);
    }

    @Override
    public void ABSExecCommand() throws FileNotFound {
        String res = "";
        File f = null;
        if (Commands.size() == 1) {
            f = WorkDir.directory.current_directory;
            //if (!WorkDir.directory.current_directory.isDirectory()) return;
        }
        else {
            try {
                f = WorkDir.directory.GetFileOrDirectory(Commands.get(1), TypeOfFile.Directory);
            }
            catch (FileNotFound err) {
                throw err;
            }
        }
        boolean key_l = FindKey("-l");
        for (File item : f.listFiles()) {
            if (item.isDirectory()) {
                WorkDir.Output.SetOutput(item.getName() + "\tкаталог"+"\t"+for_key_l(key_l,item));
            } else {
                WorkDir.Output.SetOutput(item.getName() + "\tфайл"+"\t"+for_key_l(key_l,item));
            }
        }
    }
    public String for_key_l(boolean key, File fil) {
        if (key == true) {
            return " занимаемое место: "+fil.length()+" дата модификации "+ new Date(fil.lastModified()).toString();
        }
        else {
            return "";
        }
    }

    public String ForMan() {
        return "ls need 0 or 1 argument, print content of current directory or argument's content\n" +
                "keys:\n" +
                "-l for get extended parameters of file";
    }
}
