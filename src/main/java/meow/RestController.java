package meow;

import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    public static class RestResponse{
        private String param1;
        private String date;


        private String freeCpu;
        private String usedCpu;

        public String getUsedCpu() {
            return usedCpu;
        }

        public void setUsedCpu(String usedCpu) {
            this.usedCpu = usedCpu;
        }

        public String getFreeCpu() {
            return freeCpu;
        }

        public void setFreeCpu(String freeCpu) {
            this.freeCpu = freeCpu;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        private String param2;

        private String memory;

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        private String uptime;

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

    }
    /*
    @RequestMapping(value = "/hello", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse restMethod(String name){
        Date date = new Date();


        RestResponse result = new RestResponse();
        result.setParam1("Hi");
        result.setParam2(name);
        result.setDate(date.toString());
        return result;
    }

     */



    @RequestMapping(value = "/api/stats", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse restStats(){
        RestResponse result = new RestResponse();
        Date date = new Date();
        try {
            Process v = new ProcessBuilder("neofetch", "--stdout")
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();

            Scanner stre = new Scanner(new InputStreamReader(new BufferedInputStream(v.getInputStream())));
            StringBuilder b = new StringBuilder();
            LoggerFactory.getLogger("test").info(b.toString());
            LoggerFactory.getLogger("test").info("meo");
            v.waitFor();
            v.isAlive();
            LoggerFactory.getLogger("v").info(v.toString());
            while (stre.hasNext()) {
                b.append(stre.nextLine()).append("\n");
            }
            stre.close();

            /*
            Pattern p = Pattern.compile(".*(Uptime: .+).*(Memory: .+).*");
            LoggerFactory.getLogger("p").info(p.toString());
            Matcher m = p.matcher(b);
            if (!m.matches()) {
                throw new IllegalStateException();
            }
            String uptime = m.group(0);
            String memory = m.group(1);
            result.setUptime(uptime);
            result.setMemory(memory);

             */
            result.setMemory(b.toString().substring(b.indexOf("Memory: "), b.lastIndexOf("\n")));
            //result.setUptime(b.toString().substring(b.indexOf("Uptime: "), b.indexOf('\n', "")));
        } catch (Throwable t) {
        }

        try {
            Process v = new ProcessBuilder("uptime", "-p")
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();

            Scanner stre = new Scanner(new InputStreamReader(v.getInputStream()));
            StringBuilder b = new StringBuilder();
            while (stre.hasNext()) {
                b.append(stre.nextLine()).append("\n");
            }
            result.setUptime(b.toString());
        } catch (Throwable t) {
        }

        try {
            Process v = new ProcessBuilder("mpstat")
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();

            Scanner stre = new Scanner(new InputStreamReader(v.getInputStream()));
            StringBuilder b = new StringBuilder();
            while (stre.hasNext()) {
                b.append(stre.nextLine()).append("\n");
            }
            String[] split = b.toString().split(" ");
            result.setFreeCpu(split[split.length-1]);
        } catch (Throwable t) {
        }

        result.setDate(date.toString());
        return result;
    }
}
