package main;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java版计算器
 * @author Fan
 * @date 2019-03-18 20:56
 */
public class Calculator extends JFrame {

    //当前显示为时间
    private boolean isTime=false;
    //当前数字是否为小数
    private boolean isDecimal=false;
    //当前是否为开始输入
    private boolean isStart=true;
    //参与运算的第一个数
    private double num1=0;
    //参与运算的第二个数
    private double num2=0;
    //计算结果
    private double result=0;
    //运算符
    private int operator;
    //"+"
    private static final int ADDITION=1;
    //"-"
    private static final int SUBTRACTION=2;
    //"*"
    private static final int MULTIPLICATION=3;
    //"/"
    private static final int DIVISION=4;
    //"%"
    private static final int REMAINDER=5;

    JPanel mainPanel=new JPanel();  //主体面板
    JPanel btnPanel=new JPanel();   //按键面板
    JLabel show=new JLabel();   //显示框面板

    //构造函数,初始化此计算器
    public Calculator(){

        //功能键监听器
        FunctionListener functionListener = new FunctionListener();
        //数字键监听器
        NumberListener numberListener = new NumberListener();
        //符号键监听器
        MarkListener markListener = new MarkListener();

        this.setSize(350,400);  //大小
        this.setVisible(true);  //可见
        this.setLocationRelativeTo(null);  //位置
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  //点"x"关闭
        this.setTitle("计算器");  //标题

        //主体页面面板
        mainPanel.setLayout(new BorderLayout());
        //按键面板(5行4列)
        btnPanel.setLayout(new GridLayout(5,4));
        //显示面板初始值,大小及格式
        show.setText("0");
        show.setPreferredSize(new Dimension(350,50));
        show.setFont(new Font("宋体",Font.PLAIN,25));

        //向按键面板中依次添加按键
        this.addButton("时间",functionListener);
        this.addButton("删除",functionListener);
        this.addButton("归零",functionListener);
        this.addButton("%",markListener);
        this.addButton("7",numberListener);
        this.addButton("8",numberListener);
        this.addButton("9",numberListener);
        this.addButton("+",markListener);
        this.addButton("4",numberListener);
        this.addButton("5",numberListener);
        this.addButton("6",numberListener);
        this.addButton("-",markListener);
        this.addButton("1",numberListener);
        this.addButton("2",numberListener);
        this.addButton("3",numberListener);
        this.addButton("*",markListener);
        this.addButton("0",numberListener);
        this.addButton(".",numberListener);
        this.addButton("=",markListener);
        this.addButton("/",markListener);

        //将显示框和按键加入到主体面板中
        mainPanel.add(show,"North");
        mainPanel.add(btnPanel,"Center");
        this.add(mainPanel);
    }

    //创建按键,并设置触发事件,然后添加到按键面板
    public void addButton(String content, ActionListener listener){
        JButton button = new JButton(content);
        button.setFont(new Font("宋体",Font.BOLD,20));
        button.addActionListener(listener);
        btnPanel.add(button);
    }

    /**
     * 计算器功能键的监听器
     * @author Fan
     * @date 2019-03-18 22:33
     */
    class FunctionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = e.getActionCommand();
            if("时间".equals(content)){
                isTime=true;
                show.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }else if("删除".equals(content)){
                if(isTime){
                    show.setText("0");
                    isTime=false;
                }else if(show.getText().length()>1){
                    show.setText(show.getText().substring(0,show.getText().length()-1));
                }else{
                    show.setText("0");
                }
            }else if("归零".equals(content)){
                show.setText("0");
                num1=0;
                num2=0;
                result=0;
            }
        }
    }

    /**
     * 计算器符号键监听器
     * @author Fan
     * @date 2019-03-18 22:35
     */
    class MarkListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String mark=e.getActionCommand();
            if("=".equals(mark)){
                if(isTime){
                    num2=0;
                }else{
                    num2 = Double.parseDouble(show.getText());
                }
                switch (operator){
                    case ADDITION:
                        result=num1+num2;
                        break;
                    case SUBTRACTION:
                        result=num1-num2;
                        break;
                    case MULTIPLICATION:
                        result=num1*num2;
                        break;
                    case DIVISION:
                        if(num2==0){
                            //除数为0,提示错误
                            show.setText("error");
                            isTime=true; //此处显示时间:表示的是当前显示的为错误提示
                            return;
                        }else{
                            result=num1/num2;
                        }
                        break;
                    case REMAINDER:
                        if(num2==0){
                            show.setText("error");
                            isTime=true;
                            return;
                        }else{
                            result=num1%num2;
                        }
                        break;
                    default:
                        show.setText(show.getText());
                        return;
                }
                show.setText(result+"");
                isStart=true;
                operator=0;
            }else{
                if("+".equals(mark)){
                    operator=ADDITION;
                }else if("-".equals(mark)){
                    operator=SUBTRACTION;
                }else if("*".equals(mark)){
                    operator=MULTIPLICATION;
                }else if("/".equals(mark)){
                    operator=DIVISION;
                }else if("%".equals(mark)){
                    operator=REMAINDER;
                }
                if(isTime){
                    num1=0;
                    isTime=false;
                }else{
                    num1 = Double.parseDouble(show.getText());
                }
                show.setText("0");
            }
        }
    }

    /**
     * 计算器数字键监听器
     * @author Fan
     * @date 2019-03-18 22:34
     */
    class NumberListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String num = e.getActionCommand();
            if("0".equals(show.getText())){
                //检测开始输入
                isStart=true;
            }
            if(show.getText().contains(".")){
                //检测当前为小数
                isDecimal=true;
            }else{
                isDecimal=false;
            }

            if (isTime) {  //当前显示时间
                if(".".equals(num)){
                    show.setText("0"+num);
                }else{
                    show.setText(num);
                }
                isTime = false;
            }else if(isStart){  //才开始输入
                if(".".equals(num)){
                    show.setText("0"+num);
                }else{
                    show.setText(num);
                }
            }else if(isDecimal){  //已经是一个小数了
                if(!".".equals(num)){
                    show.setText(show.getText()+num);
                }
            }else{
                show.setText(show.getText()+num);
            }
            isStart=false;
        }
    }


    /**
     * 主运行程序
     * @author Fan
     * @date 2019-03-19 15:51
    */
    public static void main(String[] args) {
        new Calculator();
    }
}
