package com.lxyg.app.customer.utils;


public class ContactUtil {
	
	/*public static void testContacts(Context context) throws Exception{  
        Uri uri = Uri.parse("content://com.android.contacts/contacts");  
        //获得一个ContentResolver数据共享的对象  
        ContentResolver reslover = context.getContentResolver();  
        //取得联系人中开始的游标，通过content://com.android.contacts/contacts这个路径获得  
        Cursor cursor = reslover.query(uri, null, null, null, null);  
          
        //上边的所有代码可以由这句话代替：Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);  
        //Uri.parse("content://com.android.contacts/contacts") == ContactsContract.Contacts.CONTENT_URI  
         StringBuilder sb = new StringBuilder();
        while(cursor.moveToNext()){  
            //获得联系人ID  
            String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));    
            //获得联系人姓名  
            String name = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));  
            //获得联系人手机号码  
            Cursor phone =   reslover.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,   
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);  
              
//            StringBuilder sb = new StringBuilder("contactid=").append(id).append(name);  
            while(phone.moveToNext()){ //取得电话号码(可能存在多个号码)  
                int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);  
                String phoneNumber = phone.getString(phoneFieldColumnIndex);  
                sb.append(phoneNumber+",");  
            }  
            
//            sb.append(phone+",");
            //建立一个Log，使得可以在LogCat视图查看结果  
           
        }  
        System.out.println("xxxxxxxxxxx--"+sb.toString());
        
//        15539228446,18539227776,15639207772,15560050700,18639210750,18958182197,18958182132,15036075820,15238684104,15839255607,18539987125,15637113621,18239268515,15639203696,13839234668,18003766218,18539269172,18739235590,13619847233,18937674681,13603929697,15139981227,18639922249,13764675056,18739256608,18551582638,15139265855,13569648682,15039246161,18639230877,18639246105,13526663413,15188347329,18623928140,+8618638572951,18738298932,15978440312,18538193311,13122926055,18737115702,15993202559,13526867820,15216849871,18221845640,15839226625,13803847093,18639233606,15903627530,18539227775,15839202527,18205176876,13598061133
//        13939298890,18637155203,18657122291,15226185973,15003801116,13916294596,18539987385,18585877668,18639205580,18803921218,18838152065,15093377852,18538557277,13007510371,15138954656,13569641718,18934703070,13613814320,13783047555,15981860910,15393711591,18538299617,15539220670,15939292287,18218061819,13653853569,13937163469,15036154227,13037643284,18439896298,13598523191,13783566684,13598062061,18639232540,18003810936,18137157307,18303929911,18137860289,18530060909,18614951666,13776333843,13803830178,15225180329,18137779982,+8618611820970,15638238590,15937132997,15225971612,13839249182,18137770591,15838269347,18695882950,
    }  */
}
