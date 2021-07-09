package com.hsbc.data;

import com.hsbc.data.message.FileOperateMessages;
import com.hsbc.data.message.InputMesages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: CurrencyProcess
 * @description: Currency Process
 * @author: wangjx
 * @create: 2021-07-07 19:53
 **/
public class CurrencyProcess {
    private static Map<String, Double> currencyMap = new ConcurrentHashMap<String, Double>();
    public static void main(String [] args)
    {
        try {
            // output a list of all the currency
            // and amounts to the console once per minute.

            SchedulerCurrencyJob cuurTimelyJob = new SchedulerCurrencyJob();
            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            service.scheduleAtFixedRate((cuurTimelyJob), 1, 1, TimeUnit.MINUTES);

            // process the currency input
            CurrencyInputThread cuurInputThread = new CurrencyInputThread();

            cuurInputThread.start();
            if (cuurInputThread.isStop()) {
                cuurTimelyJob.shutdown();
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private enum InputType {

        COLSOLE("1", "控制台输入"),
        FILE("2", "文件输入");


        /** 输入值 */
        private String value;
        /** 描述 */
        private String describe;

        private InputType(String value, String describe) {
            this.value = value;
            this.describe = describe;
        }

        public String getValue() {
            return value;
        }
    }

    private static class SchedulerCurrencyJob extends Thread {

        private boolean isStop;
        public boolean isStop() {
            return isStop;
        }
        @Override
        public void run() {
            System.out.println(InputMesages.PROMPT_INFO_DEVIDING_LINE.getMessage());
            System.out.println(FileOperateMessages.OUT_PUT_FORMAT.getMessage());
            for (Map.Entry<String, Double> entry : currencyMap.entrySet()) {
              if (entry.getValue() != 0) {
                System.out.println(entry.getKey() + " " + entry.getValue());
              }
            }
            System.out.println(InputMesages.PROMPT_INFO_DEVIDING_LINE.getMessage());
        }

        /**
         * 设置线程标识
         * @param isStop true：停止 false：不停止
         */
        public void stopThread(boolean isStop) {
            this.isStop = isStop;
        }

        public void shutdown() {
            System.exit(0);
        }

    }

    private static class CurrencyInputThread extends Thread {

        private static final String QUIT_CMD="quit";

        private String strInputType=null;
        private String[] array;
        private String currencyType;
        private double currencyValue;
        private double totalCurrencyValue;
        private BufferedReader bf;

        private boolean isStop;
        public boolean isStop() {
            return isStop;
        }

        @Override
        public void run() {

            bf = new BufferedReader(new InputStreamReader(System.in));
            // process input type selection
            int result = selectInputType();
            if (result != 0) {
                stopThread(true);
                System.exit(0);
                return;
            }
            // process Input content from File
            if (InputType.FILE.getValue().equals(strInputType)) {
                result = processInputFromFile();
                if (result != 0) {
                    stopThread(true);
                    System.exit(0);
                    return;
                }
            }
            // process Input content from Console
            processInputFromConsole();
            stopThread(true);
            System.exit(0);
        }

        /**
         * 设置线程标识
         * @param isStop true：停止 false：不停止
         */
        public void stopThread(boolean isStop) {
            this.isStop = isStop;
        }


        /*
         * process input type selection
         */
        private int selectInputType() {
            boolean quit = false;
            try
            {
                System.out.println(InputMesages.PROMPT_INFO_WELCOME.getMessage());
                System.out.println(InputMesages.PROMPT_INFO_TYPE_SELECT.getMessage());

                while (((strInputType = bf.readLine()) != null)  ) {

                    if (QUIT_CMD.equals(strInputType)) {
                        quit = true;
                        break;
                    } else {

                        if (InputType.COLSOLE.getValue().equals(strInputType)) {
                            System.out.println(InputMesages.PROMPT_INFO_INPUT.getMessage());
                            break;
                        }

                        if (InputType.FILE.getValue().equals(strInputType)) {
                            System.out.println(InputMesages.PROMPT_INFO_FILENAME.getMessage());
                            break;
                        }
                        System.out.println(InputMesages.PROMPT_INFO_TYPE_SELECT_ERROR.getMessage());
                    }
                }
                if (quit) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                return -1;
            }
        }
        /*
         * process input from File
         */
        private int processInputFromFile() {

            String strCurrencyInput=null;
            List<String> listCurrency = new ArrayList();
            List<Double> listAmount = new ArrayList();
            try
            {
                // bf = new BufferedReader(new InputStreamReader(System.in));
                boolean quit = false;
                String strCurrencyFile = null;

                while (((strCurrencyFile = bf.readLine()) != null)  ) {
                    if (QUIT_CMD.equals(strCurrencyFile)) {
                        quit = true;
                        break;
                    }

                    File currencyFile = new File(strCurrencyFile);
                    if (!currencyFile.exists()) {
                        System.out.println(FileOperateMessages.FILE_NOT_EXIST.getMessage());
                        continue;
                    }

                    // Each payment includes a currency and an amount
                    // System.out.println("Reader file");
                    BufferedReader fr = new BufferedReader(new FileReader(currencyFile));

                    // 显示行号
                    int line = 0;
                    // check the file content
                    while ((strCurrencyInput = fr.readLine()) != null) {
                        line++;
                        array = strCurrencyInput.split(" ");
                        currencyType = array[0];

                        if (currencyType.length() != 3) {
                            System.out.println(FileOperateMessages.FILE_CURRENCY_ERROR.getMessage() + line);
                            quit = true;
                            break;
                        }

                        if (array.length== 2) {
                            if (isNumeric(array[1])) {
                                currencyType = currencyType.toUpperCase();
                                currencyValue = Double.parseDouble(array[1]);
                                listCurrency.add(currencyType);
                                listAmount.add(currencyValue);
                            } else {
                                System.out.println(FileOperateMessages.FILE_AMOUNT_FORMAT_ERROR.getMessage() + line);
                                quit = true;
                                break;
                            }
                        } else {
                            System.out.println(
                                    FileOperateMessages.FILE_AMOUNT_INPUT_ERROR.getMessage() + line);
                            quit = true;
                            break;
                        }
                    }
                    // Close the file
                    fr.close();
                    if (quit) {
                        return -1;
                    }
                    // process data
                    for (int i=0; i<listCurrency.size(); i++) {
                        currencyType = listCurrency.get(i);
                        currencyValue = listAmount.get(i);
                        if (currencyMap.containsKey(currencyType)) {
                            totalCurrencyValue = currencyMap.get(currencyType) + currencyValue;
                        } else {
                            totalCurrencyValue = currencyValue;
                        }
                        currencyMap.put(currencyType,totalCurrencyValue);
                    }

                    System.out.println(InputMesages.PROMPT_INFO_DEVIDING_LINE.getMessage());
                    System.out.println(FileOperateMessages.OUT_PUT_FORMAT.getMessage());
                    for (Map.Entry<String, Double> entry : currencyMap.entrySet()) {
                        if (entry.getValue() != 0) {
                            System.out.println(entry.getKey() + " " + entry.getValue());
                        }
                    }
                    System.out.println(InputMesages.PROMPT_INFO_DEVIDING_LINE.getMessage());

                    break;
                }

                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return -1;
            }
        }

        /*
         * process input from Console
         */
        private int processInputFromConsole() {

            boolean quit = false;
            String strCurrencyInput=null;
            try
            {
                while (((strCurrencyInput = bf.readLine()) != null)  ) {
                    if (QUIT_CMD.equals(strCurrencyInput)) {
                        quit = true;
                        break;
                    }

                    array = strCurrencyInput.split(" ");
                    currencyType = array[0];

                    if (currencyType.length() != 3) {
                        System.out.println(InputMesages.PROMPT_INFO_CRRENCY_FORMAT_ERROR.getMessage());
                        continue;
                    }

                    currencyType = currencyType.toUpperCase();
                    if (array.length == 2) {
                        if (isNumeric(array[1])) {
                            currencyValue = Double.parseDouble(array[1]);
                            if (currencyMap.containsKey(currencyType)) {
                                totalCurrencyValue = (Double)currencyMap.get(currencyType) + currencyValue;
                            } else {
                                totalCurrencyValue = currencyValue;
                            }
                            currencyMap.put(currencyType,totalCurrencyValue);
                        } else {
                            System.out.println(InputMesages.PROMPT_INFO_AMOUNT_FORMAT_ERROR.getMessage());
                        }
                    } else {
                        System.out.println(InputMesages.PROMPT_INFO_AMOUNT_INPUT_ERROR.getMessage());
                    }
                }
                if (quit) {
                    return -1;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return -1;
            }
            return 0;
        }

        public boolean isNumeric(String inputData) {
            return inputData.matches("[-+]?\\d+(\\.\\d+)?");
        }
    }
}
