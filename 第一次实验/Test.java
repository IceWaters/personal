import java.util.*;

//����ɡ����й涨������
public class Test{
	private ArrayList<People> peoples = new ArrayList<People>();
	
	public Test(){		
		People p = new People("����",80,90,90);
		peoples.add(p);
		p = new People("�ŷ�",70,90,80);
		peoples.add(p);
		p = new People("����",85,95,90);
		peoples.add(p);
		p = new People("����",85,80,70);
		peoples.add(p);
		p = new People("����",85,80,75);
		peoples.add(p);		
	}
	
	//���ǻ�+����+�����ܺͽ������򣬽����д��people�����У�������һ��1��˳������
	public void setRank(){
		//����ʵ����ʵ�ַ���
		
	}
	
	//����people�е�rankֵ��peoples��������
	//��������Ҫʹ��ArrayList����ط������������JDK API����
	public void ranking(){
		//����ʵ����ʵ�ַ���
	}
	
	//����佫�嵥
	public void listRank(){
		Iterator<People> it = peoples.iterator();
		while(it.hasNext()){
			People p = it.next();
			System.out.println("����������"+p.getRank()+"����"+p.getName()+" �ǻ�("+p.getZhihui()+"),����("+p.getGongji()+"),����("+p.getFangyu()+")");
		}
	}
	
	public static void main(String[] args){
		Test t = new Test();
		t.setRank();
		t.listRank();
		System.out.println("������ʾ-----------------------------");
		t.ranking();
		t.listRank();
	}
}