const loader = document.getElementById('loader');
loader.style.display = 'none';

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function request(URL){
  loader.style.display = 'block';
    const response = await fetch(`http://localhost:8080/${URL}`,{mode: 'cors',});
  const myJson = await response.json();
  await sleep(1800);
  loader.style.display = 'none';
  return myJson;
}

async function requestPost(URL,Body){
    const response = await fetch(`http://localhost:8080/${URL}`,{method: 'POST',body:JSON.stringify(Body),mode: 'cors',});
  const myJson = await response.json();
  return myJson;
}


let ShowAllPrintersBtn = document.getElementById("ShowAllPrinters");
ShowAllPrintersBtn.addEventListener('click',async()=>{
    let reply = (await request("printers"))
    let FormatedReply = JsonStringFormater(reply)
    document.getElementById("data").textContent="";
    document.getElementById("data").textContent= FormatedReply;
    animateText()
})

function JsonStringFormater(obj){
  let answer="";
  let reply =JSON.stringify(obj, null, 2);
  [...reply].forEach(letter => {
    if(letter.includes('{'))letter="\n"
    if(letter.includes('}'))letter=""
    if(letter.includes(','))letter=""
    if(letter.includes('['))letter=""
    if(letter.includes(']'))letter=""
    if(letter.includes('"'))letter=""
    answer+=letter
  });
  return answer;
}
  
async function GetPrinterById(){
  let PrinterId= document.getElementById("GetPrinterNoJobs").value.toString();
  let reply = (await request(`printers/${PrinterId}`))
  let FormatedReply = JsonStringFormater(reply)
  document.getElementById("data").textContent="";
  document.getElementById("data").textContent= FormatedReply;
  animateText()
}

async function GetPrinterByIdAndJobs(){
  let PrinterId= document.getElementById("GetPrinter").value.toString();
  let reply = (await request(`printers/${PrinterId}/full`))
  let FormatedReply = JsonStringFormater(reply)
  document.getElementById("data").textContent="";
  document.getElementById("data").textContent= FormatedReply;
  animateText()
}

async function DisconnectPrinter(){
  let PrinterId= document.getElementById("DisconnectInput").value.toString();
 await fetch(`http://localhost:8080/printers/${PrinterId}`, {
    method: 'DELETE',
    headers: {
        'Content-type': 'application/json'
    }
});
}

async function UpdateLiveness(){
  let PrinterId= document.getElementById("LivenessInput").value.toString();
  await fetch(`http://localhost:8080/printers/${PrinterId}/liveness`, {
     method: 'POST',
     headers: {
         'Content-type': 'application/json'
     }
 });

}

async function UpdateJobStatusInput(){
  let JobId= document.getElementById("UpdateJobStatusInput").value.toString();
  await fetch(`http://localhost:8080/printjobs/${JobId}`, {
     method: 'POST',
     headers: {
         'Content-type': 'application/json'
     }
 });

}

async function GetJobDetails(){
  let JobId= document.getElementById("GetJobDetails").value.toString();
  let reply = (await request(`printjobs/${JobId}`))
  let FormatedReply = JsonStringFormater(reply)
  document.getElementById("data").textContent="";
  document.getElementById("data").textContent= FormatedReply;
  animateText()
}

async function FilterStatus(){
  let PrinterId= document.getElementById("FilterSoloId").value.toString();
  let status= document.getElementById("FilterStatusInputSolo").selectedOptions[0].textContent;
  let path="";
  if(status.includes("Both"))
  path=''
  else
  path=`?status=${status}`
  let reply = (await request(`printers/${PrinterId}/printjobs${path}`))
  let FormatedReply = JsonStringFormater(reply)
  document.getElementById("data").textContent="";
  document.getElementById("data").textContent= FormatedReply;
  animateText()
}


async function FilterStatusDate(){
  let date= document.getElementById("timeInput").value;
  let status= document.getElementById("FilterStatusInput").selectedOptions[0].textContent;
  let path="";
  if(!status.includes("Both")||!date.includes("1999-09-30T01:00"))
  path="?";
  if(!status.includes("Both"))
  path+=`status=${status}&`
  if(!date.includes("1999-09-30T01:00"))
  path+=`since=${date}:00`
  console.log(path)
  let reply = (await request(`printjobs${path}`))
  let FormatedReply = JsonStringFormater(reply)
  document.getElementById("data").textContent="";
  document.getElementById("data").textContent= FormatedReply;
  animateText()
}

async function GetStats(){
  let reply = (await request(`statistics`))
  let FormatedReply = JsonStringFormater(reply)
  document.getElementById("data").textContent="";
  document.getElementById("data").textContent= FormatedReply;
  animateText()
}

async function AddPrinterWithBody(){
  let id = document.getElementById("AddPrinterInput").value;
  await fetch('http://localhost:8080/printers', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json'
  },
   body: `{id:${id}}`
})
}

async function AddJobAndText(){
  let Text = document.getElementById("JobStringInput").value;
  let id = document.getElementById("AddJobIdInput").value;
  await fetch(`http://localhost:8080/printers/${id}/printjobs`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      data : Text
    })
  }).then(response => response.json())
  .then(data => 
    {document.getElementById("data").textContent="";
    document.getElementById("data").textContent= 'The new Job Id is: ' + data.toString();
    animateText()})
  }










//css typing


function createTextNodeWithAnimation(letter){
  if(letter=='\n')
  {
      let textBox = document.getElementById("data");
      let textNode= document.createElement('div')
  textBox.appendChild(textNode)
      return
  }

  if(letter==' ')
  {
      let textBox = document.getElementById("data");
      let textNode= document.createElement('span')
      textNode.textContent= 's'
      textNode.id="space"
      textNode.innerHTML+=`
      <style>
          #space{
              color:transparent;
              font-size:10px;
      }
      </style>
  `
  textBox.appendChild(textNode)
      return
  }
  let textBox = document.getElementById("data");
  let textNode= document.createElement('span')
  textNode.textContent= letter
  textNode.id="textNode"
  textNode.innerHTML+=`
      <style>
          #textNode{
              animation: animateLetter 1.2s 1;
              margin-right: -6px;
      }
      </style>
  `
  textBox.appendChild(textNode)
}

async function WriteString(string){
  let textBox = document.getElementById("data");
  textBox.textContent='';
  for(i=0;i<string.length;i++){
      if(i%100==0)
      await sleep(1)    
      await createTextNodeWithAnimation(string[i])
  }
}

async function animateText(){
  let string =document.getElementById("data").textContent.toString();
  await WriteString(string)
}