/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moneymanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Shathin
 */
public class AppBrain {
    
    public static void insertIncome(String date, String time, String account, String incomeCategory, double amount){
       
        File income = new File("files","income.moneymanager");
                
        try{
            
            FileWriter incomeWriter = new FileWriter(income, true);
            BufferedWriter incomeOut = new BufferedWriter(incomeWriter);
            String inputString;
            
            inputString = date + "|" + time + "|" + account + "|" + incomeCategory + "|" + amount;
            incomeOut.write(inputString);
            incomeOut.newLine();
            incomeOut.close();
            
            changeAccountStatus(account,amount);
            
        }
        catch(Exception e){
            System.out.println("Exception found in insertIncome "+e);
            
        }
    }
    
    public static void insertExpense(String date, String time, String account, String expenseCategory, double amount){
        
        File expense = new File("files","expense.moneymanager");
                
        try{
            
            FileWriter expenseWriter = new FileWriter(expense, true);
            BufferedWriter expenseOut = new BufferedWriter(expenseWriter);
            String inputString;
            inputString = date + "|" + time + "|" + account + "|" + expenseCategory + "|" + amount;
            expenseOut.write(inputString);
            expenseOut.newLine();
            expenseOut.close();
            
            changeAccountStatus(account,(-amount));
        }
        catch(Exception e){
        
            System.out.println("Insert Expense "+e);
        }
        
    }
    
    public static void doTransfer(String date, String time, String fromAccount, String toAccount, double amount){
        
        File transfer = new File("files","transfer.moneymanager");
                
        try{
            
            FileWriter transferWriter = new FileWriter(transfer, true);
            BufferedWriter transferOut = new BufferedWriter(transferWriter);
            String inputString;
            
            inputString = date + "|" + time + "|" + fromAccount + "|" + toAccount + "|" + amount;
            transferOut.write(inputString);
            transferOut.newLine();
            transferOut.close();
            
            changeAccountStatus(toAccount,amount);
            changeAccountStatus(fromAccount,(-amount));
        }
        catch(Exception e){}
        
    }
    
    public static void changeAccountStatus(String account, double additionalAmount){
        
        File accounts = new File("files","accounts.moneymanager");
        
        try{
            FileReader accountsReader = new FileReader(accounts);
            BufferedReader accountsbr = new BufferedReader(accountsReader);
            
            ArrayList<String> accountsList = new ArrayList<String>();
            
            String accountsString = "";
            
            while((accountsString = accountsbr.readLine())!=null){
                accountsList.add(accountsString);
            }
            String matchedString = "";     
            for(String compareString : accountsList){
                if(compareString.contains(account)){
                    matchedString = compareString;
                    accountsList.remove(compareString);
                    break;
                }
            }

            FileWriter accountsWriter = new FileWriter(accounts);
            BufferedWriter accountswr = new BufferedWriter(accountsWriter);
            
            StringTokenizer aToken = new StringTokenizer(matchedString, "|");
            aToken.nextToken();
            accountsList.add(account+"|"+((Double.parseDouble(aToken.nextToken())+additionalAmount)));
            
            for(String updatedString : accountsList){
                accountswr.write(updatedString);
                accountswr.newLine();
            }
            accountswr.close();
        }
        catch(Exception e){
            System.out.println("Change account status "+e);
        }
    }
    
    public static boolean checkAccountBalance(String account, double amountCheck){
        try{
            File accounts = new File("files","accounts.moneymanager");
            FileReader accountsReader = new FileReader(accounts);
            BufferedReader accountsbr = new BufferedReader(accountsReader);
            String inputString ="";
            while((inputString = accountsbr.readLine()) != null){
                StringTokenizer st = new StringTokenizer(inputString,"|");
                String accountString = st.nextToken();
                if(accountString.compareTo(account) == 0){
                    String amountString = st.nextToken();
                    Double amount = Double.parseDouble(amountString);
                    if(amountCheck < amount)
                        return true;
                }
            }
        }
        catch(Exception e){
            System.out.println("Exception found in checkAccountBalance: "+e);
        }
        return false;
    }
        
    public static void generateMiniStatement(String account){
        
        File income = new File("files","income.moneymanager");
        File expense = new File("files","expense.moneymanager");
        File transfer = new File("files","transfer.moneymanager");
        File miniStatement = new File("files","miniStatement.moneymanager");
        
        try{
            FileReader incomeReader = new FileReader(income);
            FileReader expenseReader = new FileReader(expense);
            FileReader transferReader = new FileReader(transfer);
            
            BufferedReader incomebr = new BufferedReader(incomeReader);
            BufferedReader expensebr = new BufferedReader(expenseReader);
            BufferedReader transferbr = new BufferedReader(transferReader);
            
            FileWriter miniStatementWriter = new FileWriter(miniStatement);
            
            BufferedWriter miniStatementbw = new BufferedWriter(miniStatementWriter);
            
            String incomeString = "", expenseString = "",  transferString = "";
            
            while((incomeString = incomebr.readLine()) != null){
                  StringTokenizer iToken = new StringTokenizer(incomeString, "|");
                  String date = iToken.nextToken(); 
                  String time = iToken.nextToken();
                  if((iToken.nextToken()).equals(account)){
                      String typeOfTransaction = "Income";
                      String category = iToken.nextToken();
                      String amount = iToken.nextToken();
                      miniStatementbw.write(date +"|"+ time +"|" + typeOfTransaction +"|"+ category +"|"+ amount);
                      miniStatementbw.newLine();
                  }     
            }
            
            while((expenseString = expensebr.readLine()) != null){
                StringTokenizer eToken = new StringTokenizer(expenseString, "|");
                String date = eToken.nextToken(); 
                String time = eToken.nextToken();
                if((eToken.nextToken()).equals(account)){
                    String typeOfTransaction = "Expense";
                    String category = eToken.nextToken();
                    String amount = eToken.nextToken();
                    miniStatementbw.write(date +"|"+ time +"|" + typeOfTransaction +"|"+ category +"|"+ amount);
                    miniStatementbw.newLine();
                }
            }
            
            while((transferString = transferbr.readLine()) != null){
                StringTokenizer tToken = new StringTokenizer(transferString, "|");
                String date = tToken.nextToken(); 
                String time = tToken.nextToken(); 
                String fromAccount = tToken.nextToken();
                String toAccount = tToken.nextToken();
                String amount = tToken.nextToken();
                if(toAccount.equals(account)){
                    
                    miniStatementbw.write(date + "|" + time + "|" + "Transfer" + "|"+ "Transfered from "+toAccount + "|"+ amount);
                    miniStatementbw.newLine();
                }
                if(fromAccount.equals(account)){
                    miniStatementbw.write(date + "|" + time + "|" + "Transfer" + "|"+ "Transfered to "+toAccount + "|"+ amount);
                    miniStatementbw.newLine();
                }
            }
            miniStatementbw.close();
            
            sortMiniStatement();
            
        }
        catch(Exception e){}
        
    }
    
    public static void sortMiniStatement(){
        
        File miniStatement = new File("files","miniStatement.moneymanager");
        
        try{
            FileReader miniStatementReader = new FileReader(miniStatement);
            BufferedReader miniStatementbr = new BufferedReader(miniStatementReader);
            
            ArrayList<String> miniStatementList = new ArrayList<String>();
            
            String miniStatementString = "";
            
            while(((miniStatementString = miniStatementbr.readLine()) != null) && miniStatementString.compareTo(System.lineSeparator()) > 0){
                    miniStatementList.add(miniStatementString);               
            }
            
            Collections.sort(miniStatementList);
                        
            FileWriter miniStatementWriter = new FileWriter(miniStatement);
            BufferedWriter miniStatementwr = new BufferedWriter(miniStatementWriter); 
            
            for(int i=0;i<miniStatementList.size();i++){
                miniStatementwr.write(miniStatementList.get(i));
                miniStatementwr.newLine();
            }
            miniStatementwr.close();    
        }
        catch(Exception e){}
                
    }
    
    public static String[][] obtainMiniStatementByDate(String startDate, String endDate){
        File miniStatement = new File("files","miniStatement.moneymanager");
        int lineCount = obtainNumberOfLines(miniStatement);
        int fieldCount = obtainNumberOfFields(miniStatement);
        String [][]outputArray = new String[lineCount][fieldCount];
        try{
            BufferedReader miniReader = new BufferedReader(new FileReader(miniStatement));
            String inputString = "";
            int arrayIndex = 0;        
            for(int i=0; i<lineCount; i++){
                inputString = miniReader.readLine();
                StringTokenizer miniToken = new StringTokenizer(inputString,"|");
                String date = miniToken.nextToken();
                if(startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0){
                    outputArray[arrayIndex][0] = date;
                    for(int j=1; j<fieldCount; j++){
                        outputArray[arrayIndex][j] = miniToken.nextToken();
                    }
                    arrayIndex++;
                }
            }
            miniReader.close();
        }
        catch(Exception e){
            System.out.println("Exception found in obtainMiniStatementByDate! "+e);
        }
        
        return outputArray;
    }
/* ---------------------------------------------------------------------------------------------------*/
    public static void addAccount(String accountName){
        try{
            File account = new File("files","accounts.moneymanager");
            BufferedWriter bw = new BufferedWriter(new FileWriter(account,true));
            
            try{
                double initialAmount = 0;
                if(accountExists(account,accountName) == false){
                    bw.write(accountName +"|"+ initialAmount);
                    bw.newLine();
                    bw.close();
                }
            }
            catch(NumberFormatException nfe){
                System.out.println("Enter valid amount");
            }
            
        }
        catch(Exception e){
            System.out.println("Exception found in addAccount: "+e);
        }
    }
    
    public static boolean accountExists(File filename, String accountName){
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String inputString = "";
            while((inputString = br.readLine()) != null){
                StringTokenizer st = new StringTokenizer(inputString,"|");
                if(accountName.compareTo(st.nextToken()) == 0)
                    return true;
            }
        }
        catch(Exception e){
            System.out.println("Exception found in accountExists: "+e);
        }
        return false;
    }
    
    public static void addIncomeCategory(String newIncomeCategory){
        
        File incomeCategory = new File("files","incomeCategory.moneymanager");
        
        addCategory(incomeCategory,newIncomeCategory);
  
    }
    
    public static void addExpenseCategory(String newExpenseCategory){
        
        File expenseCategory = new File("files","expenseCategory.moneymanager");
        
        addCategory(expenseCategory,newExpenseCategory);
        
    }
    
    public static void addCategory(File categoryFile, String newCategory){
        
        try{
            FileWriter categoryWriter = new FileWriter(categoryFile, true);
            BufferedWriter categorybw = new BufferedWriter(categoryWriter);
            
            if(!categoryExists(categoryFile,newCategory)){

                categorybw.write(newCategory);
                categorybw.newLine();
                categorybw.close();
            }
        }
        catch(Exception e){}
    }
    
    public static boolean categoryExists(File filename, String categoryName){
        try{
            FileReader categoryReader = new FileReader(filename);
            BufferedReader categorybr = new BufferedReader(categoryReader);
            
            ArrayList<String> categoryList = new ArrayList<String>();
            String category;
            
            while((category = categorybr.readLine()) != null){
                categoryList.add(category);
            }
            
            if(!categoryList.contains(categoryName))
                return false;
        }
        catch(Exception e){}
        
        return true;
    }
    

    public static void deleteIncomeCategory(String incomeCategoryName){
        
        File incomeCategory = new File("files","incomeCategory.moneymanager");
        
        deleteCategory(incomeCategory,incomeCategoryName);
        
    }
    
    public static void deleteExpenseCategory(String expenseCategoryName){
        
        File expenseCategory = new File("files","expenseCategory.moneymanager");
        
        deleteCategory(expenseCategory,expenseCategoryName);
        
    }
    
    public static void deleteCategory(File categoryFile, String oldCategory){
        
        try{
            FileReader categoryReader = new FileReader(categoryFile);
            BufferedReader categorybr = new BufferedReader(categoryReader);
            
            String categoryString = "";
            
            ArrayList<String> categoryList = new ArrayList<String>();
            
            while((categoryString = categorybr.readLine()) != null){
                categoryList.add(categoryString);
            }
            
            categoryList.remove(oldCategory);
            
            FileWriter categoryWriter = new FileWriter(categoryFile);
            BufferedWriter categorybw = new BufferedWriter(categoryWriter);
            
            for(String category : categoryList){
                categorybw.write(category);
                categorybw.newLine();
            }
            categorybw.close();
        }
        catch(Exception e){}
    }
    
    public static void deleteAccount(String accountName){
        try{
            File account = new File("files","accounts.moneymanager");
            BufferedReader br = new BufferedReader(new FileReader(account));
            String inputString = "";
            ArrayList<String> accountList = new ArrayList<>();
            while((inputString = br.readLine()) != null){
                accountList.add(inputString);
            }
            int position = -1;
            for(String s : accountList){
                String a = (new StringTokenizer(s,"|")).nextToken();
                if(a.compareTo(accountName) == 0)
                    position = accountList.indexOf(s);
            }
            if(position != -1)
                accountList.remove(position);
            BufferedWriter bw = new BufferedWriter(new FileWriter(account));
            for(String s : accountList){
                bw.write(s);
                bw.newLine();
            }
            bw.close();
        }
        catch(Exception e){
            System.out.println("Exception found in deleteAccount: "+e);
        }
    }
    
    public static double calculateTotalIncome(){
        
        File income = new File("files","income.moneymanager");
        
        return calculate(income);
        
    }
        
    public static double calculateTotalExpense(){
        
        File expense = new File("files","expense.moneymanager");
        
        return calculate(expense);
        
    }
    
    public static double calculate(File filename){
        
        double total = 0;
        
        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            
            String data = "";
            while((data = br.readLine()) != null){
                StringTokenizer token = new StringTokenizer(data,"|");
                for(int i = 0; i < 4; i++){
                   String s = token.nextToken();
                }
                String amount = token.nextToken();
                total = total + Double.parseDouble(amount);
            }
        }
        catch(Exception e){
        
            System.out.println("Exception found in calculate "+e);
        }
        return total;
    }
    
    public static double calculateTotal(double totalIncome, double totalExpense){
        return (totalIncome - totalExpense);
    }
    
    
    public static void performKWayMerge(){
        
        File income = new File("files","income.moneymanager");
        File expense = new File("files","expense.moneymanager");
        File displayFile = new File("files","displayFile.moneymanager");
        try{
            FileReader incomeReader = new FileReader(income);
            FileReader expenseReader = new FileReader(expense);
            
            FileWriter displayWriter = new FileWriter(displayFile);
            
            BufferedReader incomebr = new BufferedReader(incomeReader);
            BufferedReader expensebr = new BufferedReader(expenseReader);
            
            BufferedWriter displaywr = new BufferedWriter(displayWriter);
            
            String incomeString = incomebr.readLine(), expenseString = expensebr.readLine();
            while(incomeString !=null || expenseString != null){
                if(incomeString == null)
                    incomeString = "~";
                if(expenseString == null)
                    expenseString ="~";
                if(incomeString == "~" && expenseString =="~")
                    break;
                
                if((incomeString.compareTo(expenseString) < 0)){
                    displaywr.write(incomeString + "|Income" );
                    displaywr.newLine();
                    incomeString = incomebr.readLine();
                }
                else if(incomeString.compareTo(expenseString) > 0 ){
                    displaywr.write(expenseString + "|Expense" );
                    displaywr.newLine();
                    expenseString = expensebr.readLine();
                }
            }
            displaywr.close();
            incomebr.close();
            expensebr.close();
        }
        catch(Exception e){
            System.out.println("Exception found in performKWayMerge:" +e);
        }
    }
    
    public static String[][] obtainArray(File filename, String caller){
        
       int lineCount = obtainNumberOfLines(filename);
       int fieldCount = obtainNumberOfFields(filename);
//       if(caller.compareTo("MiniStatement Table") == 0)
//           System.out.println("Line Count: "+lineCount+"\nFieldCount: "+fieldCount);
       String[][] outputArray = new String[lineCount][fieldCount];
       
       try{
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String inputString;
            
            for(int i=0; i<lineCount; i++){
                inputString = br.readLine();
                StringTokenizer st = new StringTokenizer(inputString, "|");
                for(int j=0; j<fieldCount; j++){
                    outputArray[i][j] = st.nextToken();
//                    if(caller.compareTo("MiniStatement Table") == 0)
//                        System.out.println(outputArray[i][j]);
                }
            }            
        }
       
        catch(Exception e){
        System.out.println("Obtain Array Exception " +e+"\n Caller "+caller);
        }
        
        return outputArray; 
    }
    
    public static int obtainNumberOfLines(File filename){
        int count = 0;
        try{
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String inputString = "";
            
            while((inputString = br.readLine())!=null){
                count++;
            }
        }
        catch(Exception e){
        System.out.println("Exception found in obtainNumberOfLines: "+e);
        }
        
        return count; 
    }
    
    public static int obtainNumberOfFields(File filename){
        int count = 0;
        try{
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String inputString = br.readLine();
            if(inputString != null)
            {   
                StringTokenizer st = new StringTokenizer(inputString,"|");
                count = st.countTokens();
            
            }
        }
        catch(Exception e){
        System.out.println("Exception found in obtainNumberOfFields "+e);
        }
        
        return count; 
        
    }
    
    public static String[] obtainAccounts(){
        
        File accounts = new File("files","accounts.moneymanager");
        String[] outputArray = new String[obtainNumberOfLines(accounts)];
        
        try{
            FileReader reader = new FileReader(accounts);
            BufferedReader br = new BufferedReader(reader);
            String inputString = "";
            int i = 0;
            while((inputString = br.readLine())!=null){
                StringTokenizer st = new StringTokenizer(inputString,"|");
                outputArray[i++] = st.nextToken();
            }
            
        }
        catch(Exception e){
        System.out.println("Obtain Account Exception");
        }
        
        return outputArray;
    }
    
    public static String[] obtainCategories(File filename){
        
        String[] outputArray = new String[obtainNumberOfLines(filename)];
        
        try{
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String inputString = "";
            int i = 0;
            while((inputString = br.readLine())!=null){
                  outputArray[i++] = inputString;
            }
            
        }
        catch(Exception e){
        System.out.println("Obtain Category Exception");
        }
        
        return outputArray;
    }
    
    public static String[][] obtainAccountBalance(File filename){
        String output[][] = new String[obtainNumberOfLines(filename)][obtainNumberOfLines(filename)];
        String line;
        int lineCount = obtainNumberOfLines(filename);
        try
        {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            for(int i=0;i<lineCount;i++)
            {
                line = br.readLine();
                StringTokenizer st = new StringTokenizer(line, "|");
                for(int j=0;j<2;j++)
                {
                    output[i][j] =  st.nextToken();
                }
            }
            
        }
        catch(Exception e)
        {
            System.out.print("Acount balance "+e);
        }
        return output;
    }
    
       
    public static void copyFiles(String fname){
        
        try
        {
            String filenames[] = {"accounts.moneymanager", "expense.moneymanager", "displayFile.moneymanager", "expenseCategory.moneymanager", "income.moneymanager", "incomeCategory.moneymanager", "miniStatement.moneymanager", "transfer.moneymanager"};
        
            FileReader fr;
            BufferedReader br; 
            FileWriter fw ; 
            BufferedWriter bw;
            for(String s : filenames)
            {
                fr = new FileReader(new File("files",s));
                br = new BufferedReader(fr);
                fw = new FileWriter(new File(fname,s));
                bw = new BufferedWriter(fw);
                System.out.println("Copying : "+s);
                String str = br.readLine();
                while(str != null)
                {
                    System.out.println(str);
                    bw.write(str);
                    bw.newLine();
                    str = br.readLine();
                }
                bw.close();
                 
            }   
            //bw.close();
        }
        catch(Exception e)
        {
            
        }

        
    }
    
    public static void reset(){
        try{
            String bakFileName = "Backup"+String.valueOf(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            new File(bakFileName).mkdir();
            copyFiles(bakFileName);
            String filenames[] = {"accounts.moneymanager", "expense.moneymanager", "displayFile.moneymanager", "expenseCategory.moneymanager", "income.moneymanager", "incomeCategory.moneymanager", "miniStatement.moneymanager", "transfer.moneymanager"};
            for(String s : filenames){
                FileWriter fw = new FileWriter(new File("files",s));
                fw.close();
            }
            System.out.println("Cleared!");
        }
        catch(Exception e){
            System.out.println("Exception found in reset: "+e);
        }
    }
}
