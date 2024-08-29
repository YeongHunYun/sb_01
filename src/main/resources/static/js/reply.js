/*
axios앞에는 await를 붙여주고
함수 내에서 axios를 사용할 때는
함수 명 앞에 async를 붙여준다

 */

async function get1(bno){
    const result = await  axios.get(`/replies/list/${bno}`);
    // console.log(result);
    return result.data;
}

async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`, {params:{page, size}});

    if(goLast){
        const total = result.data.total;
        const lastPage = parseInt(Math.ceil(total/size));
        return getList({bno:bno, page:lastPage,size:size});
    }

    return result.data;
}

async function addReply(replyObj){
    const response = await axios.post('/replies/', replyObj);
    return response.data;
}

async function getReply(rno){
    const response = await axios.get(`/replies/${rno}`);
    return response.data;
}

async function modifyReply(replyObj){
    const response=await axious.put(`/replies/${replyObj.rno}`, replyObj);
    return response.data;
}


async function removeReply(rno){
    const response = await axios.delete(`/replies/${rno}`);
    return response.data;
}