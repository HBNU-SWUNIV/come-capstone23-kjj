import styled from "styled-components";

const Wrapper = styled.div`
    position:absolute;
    width:100vw;
    height:200vh;
    background-color:rgba(0,0,0,0.7);
    opacity:0.7;
`;


function Overlay(){
    return(
        <Wrapper/>
    )
}

export default Overlay;