import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmdListenRestart {
    private static volatile boolean flg = false;
    private static Date date = new Date();
    public static void main(String[] args) throws IOException {
        while(true){
            List<String> check = new ArrayList<>();
            check.add("cmd.exe");
            check.add("/c");
            check.add("netstat -ano|findstr 0.0.0.0:"+args[0]);
            ProcessBuilder pb =new ProcessBuilder(check);
            Process process = pb.start();
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            if(System.currentTimeMillis() - date.getTime() > 60000){
                System.out.println("存活监听中");
                System.out.println(line);
                date = new Date();
            }
            if (line!=null){
                continue;
            }
            if (!flg) {
                if (flg) {
                    continue;
                }
                flg = true;
                new Thread(() -> {
                    try {
                        List<String> restart = new ArrayList<>();
                        restart.add("cmd.exe");
                        restart.add("/c");
                        restart.add("start /b java -jar "+args[1]);
                        System.out.println(new Date());
                        ProcessBuilder pb1 = new ProcessBuilder(restart);
                        Process process1 = pb1.start();
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(process1.getInputStream(), "GBK"));
                        String s;
                        while ((s = br1.readLine()) != null) {
                            System.out.println(s);
                        }
                        flg=false;
                    }catch (Exception e){
                        flg=false;
                    }
                }).start();
            }
        }
    }
}
