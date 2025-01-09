package PB;

import java.io.*;
import java.util.*;

class PhoneEntry {
	private String name;
	private String phoneNumber;

	public PhoneEntry(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	public String getName() { return name; }
	public String getPhoneNumber() { return phoneNumber; }
	
	@Override
	public String toString() { return "이름: " + name + "전화번호: " + phoneNumber; }
}

public class PhoneBookManager {
	private static final String FILE_NAME = "phonebook.txt";
	private List<PhoneEntry> phoneBook = new ArrayList<>();
	
	public static void main(String[] args) {
		PhoneBookManager manager = new PhoneBookManager();
		manager.loadFromFile();
		manager.run();
	}
	
	public void run() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("\n--- 전화번호 관리 ---");
			System.out.println("1. 전화번호 저장");
			System.out.println("2. 전화번호 수정");
			System.out.println("3. 전화번호 목록");
			System.out.println("4. 전화번호 삭제");
			System.out.println("5. 종료");
			
			System.out.print("> ");
			int choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
				case 1:
					addPhoneNumber(sc);
					break;
				case 2:
					editPhoneNumber(sc);
					break;
				case 3:
					displayPhoneNumbers();
					break;
				case 4:
					deletePhoneNumber(sc);
					break;
				case 5:
					saveToFile();
					System.out.println("프로그램 종료");
					return;
				default:
					System.out.println("잘못된 선택입니다. 다시 시도하세요");
			}
		}
	}
	
	private void addPhoneNumber(Scanner sc) {
		System.out.println("이름: ");
		String name = sc.nextLine();
		System.out.println("전화번호: ");
		String PhoneNumber = sc.nextLine();
		phoneBook.add(new PhoneEntry(name, PhoneNumber));
		System.out.println("전화번호가 저장되었습니다");
		saveToFile();
	}
	
	private void editPhoneNumber(Scanner sc) {
	    System.out.println("수정할 이름: ");
	    String nameToEdit = sc.nextLine();

	    List<PhoneEntry> matchingEntries = new ArrayList<>();
	    for (PhoneEntry entry : phoneBook) {
	        if (entry.getName().equals(nameToEdit)) {
	            matchingEntries.add(entry);
	        }
	    }

	    if (matchingEntries.isEmpty()) {
	        System.out.println("해당 이름의 전화번호를 찾을 수 없습니다");
	        return;
	    }

	    if (matchingEntries.size() > 1) {
	        System.out.println("같은 이름을 찾았습니다. 번호를 선택해주세요");
	        for (int i = 0; i < matchingEntries.size(); i++) {
	            System.out.println((i + 1) + ". " + matchingEntries.get(i));
	        }

	        System.out.println("번호를 입력하세요: ");
	        int choice = sc.nextInt();
	        sc.nextLine();

	        if (choice < 1 || choice > matchingEntries.size()) {
	            System.out.println("잘못된 선택입니다");
	            return;
	        }

	        PhoneEntry selectedEntry = matchingEntries.get(choice - 1);
	        System.out.println("현재 전화번호: " + selectedEntry.getPhoneNumber());
	        System.out.println("새로운 전화번호: ");
	        String newPhoneNumber = sc.nextLine();
	        phoneBook.set(phoneBook.indexOf(selectedEntry), new PhoneEntry(selectedEntry.getName(), newPhoneNumber));
	        System.out.println("전화번호가 수정되었습니다");
	    } else {
	        PhoneEntry entry = matchingEntries.get(0);
	        System.out.println("현재 전화번호: " + entry.getPhoneNumber());
	        System.out.println("새로운 전화번호: ");
	        String newPhoneNumber = sc.nextLine();
	        phoneBook.set(phoneBook.indexOf(entry), new PhoneEntry(entry.getName(), newPhoneNumber));
	        System.out.println("전화번호가 수정되었습니다");
	    }

	    saveToFile();
	}


	
	private void displayPhoneNumbers() {
		if(phoneBook.isEmpty()) {
			System.out.println("저장된 전화번호가 없습니다");
		} else {
			System.out.println("\n--- 전화번호 목록 ---");
			for(PhoneEntry entry : phoneBook) {
				System.out.println(entry);
			}
		}
	}
	
	private void deletePhoneNumber(Scanner sc) {
	    System.out.println("삭제할 이름: ");
	    String nameToDelete = sc.nextLine();

	    List<PhoneEntry> matchingEntries = new ArrayList<>();
	    for (PhoneEntry entry : phoneBook) {
	        if (entry.getName().equals(nameToDelete)) {
	            matchingEntries.add(entry);
	        }
	    }

	    if (matchingEntries.isEmpty()) {
	        System.out.println("해당 이름의 전화번호를 찾을 수 없습니다");
	        return;
	    }

	    if (matchingEntries.size() > 1) {
	        System.out.println("같은 이름을 찾았습니다. 번호를 선택해주세요");
	        for (int i = 0; i < matchingEntries.size(); i++) {
	            System.out.println((i + 1) + ". " + matchingEntries.get(i));
	        }

	        System.out.print("> ");
	        int choice = sc.nextInt();
	        sc.nextLine();

	        if (choice < 1 || choice > matchingEntries.size()) {
	            System.out.println("잘못된 선택입니다");
	            return;
	        }

	        PhoneEntry selectedEntry = matchingEntries.get(choice - 1);
	        phoneBook.remove(selectedEntry);
	        System.out.println("전화번호가 삭제되었습니다");
	    } else {
	        phoneBook.remove(matchingEntries.get(0));
	        System.out.println("전화번호가 삭제되었습니다");
	    }

	    saveToFile();
	}


	
	private void loadFromFile() {
		File file = new File(FILE_NAME);
		if(!file.exists()) {
			return;
		}
		try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
			String line;
			while((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if(parts.length == 2) {
					phoneBook.add(new PhoneEntry(parts[0], parts[1]));
				}
			}
		} catch(IOException e) {
			System.out.println("파일 읽기 오류: " + e.getMessage());
		}
	}
	
	private void saveToFile() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
			for(PhoneEntry entry : phoneBook) {
				writer.write(entry.getName() + "," + entry.getPhoneNumber());
				writer.newLine();
			}
		} catch(IOException e) {
			System.out.println("파일 쓰기 오류: " + e.getMessage());
		}
	}
}