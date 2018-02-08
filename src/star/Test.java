package star;



public class Test {
	public int aMe(){
		static int i=1;
		i++;
		return i;
		}
	//测试函数  
    public static void main(String[] args) {  
        /*DrawBorder db = new DrawBorder();  
        db.initFrame();  */
    	Test test = new Test();
    	test.aMe();
    	int j = test.aMe();
    	System.out.println(j);
    	
    	
}
}
