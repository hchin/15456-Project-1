public class MultiKeyReader
{

  HashMap keyMap;
  boolean [] keySet;
  ArrayList keyQueue; 
  int noKeys, curEventNo;

  public MultiKeyReader(char[] keys, int noKeys)
  {
    keySet = new boolean[keys.length];
    this.noKeys = noKeys;
    keyMap = new HashMap();
    keyQueue = new ArrayList(noKeys);
    for (int i=0;i<keys.length;i++)
      keyMap.put(keys[i],i);
    curEventNo = 0;

  }

  void onKeyPressed()
  {
    if(keyMap.containsKey(key))
    {
      if(!keyQueue.contains(key))
      {
        keySet[(Integer)keyMap.get(key)] = true;
        curEventNo += pow(2,(Integer)keyMap.get(key)); 

        if(keyQueue.size()==noKeys)
         { //the queue is full and we need to remove the first ke
          keyQueue.remove(0); //removes the first key
         }
        //adds the key to the queue
        keyQueue.add(key);
      }
    }
  }

  void onKeyReleased()
  {
    if(keyMap.containsKey(key))
    {
      keySet[(Integer)keyMap.get(key)] = false;
      keyQueue.remove(keyQueue.indexOf(key));
      curEventNo -= pow(2,(Integer)keyMap.get(key)); 
    }
  }

  boolean [] getEventArray()
  {
    return keySet;
  }

  int getEventNo()
  {
    return curEventNo;
  }
  	
  String toString()
  {
    StringBuffer sb = new StringBuffer();
    for(int i=0;i<keyQueue.size();i++)
      sb.append(" "+keyQueue.get(i));
    return sb.toString();
  }

}

