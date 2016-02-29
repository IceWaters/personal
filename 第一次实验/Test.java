import java.util.*;

//请完成★★★中规定的内容
public class Test{
	private ArrayList<People> peoples = new ArrayList<People>();
	
	public Test(){		
		People p = new People("关羽",80,90,90);
		peoples.add(p);
		p = new People("张飞",70,90,80);
		peoples.add(p);
		p = new People("赵云",85,95,90);
		peoples.add(p);
		p = new People("黄忠",85,80,70);
		peoples.add(p);
		p = new People("刘备",85,80,75);
		peoples.add(p);		
	}
	
	//按智慧+攻击+防御总和进行排序，将序号写入people对象中，排名第一是1，顺序增加
	public void setRank(){
		//★★★实验者实现方法
		
	}
	
	//按照people中的rank值对peoples进行排序
	//该排序需要使用ArrayList的相关方法，具体查阅JDK API帮助
	public void ranking(){
		//★★★实验者实现方法
	}
	
	//输出武将清单
	public void listRank(){
		Iterator<People> it = peoples.iterator();
		while(it.hasNext()){
			People p = it.next();
			System.out.println("三国排名【"+p.getRank()+"】："+p.getName()+" 智慧("+p.getZhihui()+"),攻击("+p.getGongji()+"),防御("+p.getFangyu()+")");
		}
	}
	
	public static void main(String[] args){
		Test t = new Test();
		t.setRank();
		t.listRank();
		System.out.println("排序显示-----------------------------");
		t.ranking();
		t.listRank();
	}
}