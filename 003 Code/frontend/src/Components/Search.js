import styled from "styled-components"
import { FaSearch } from "react-icons/fa";
import {useForm} from 'react-hook-form';

const SearchW = styled.div`
    display:flex;
    justify-content:space-between;
    align-items:center;
    width:250px;
    height:50px;
    margin-top:20px;
    border-radius:20px;
    margin-left:120px;
    background-color:#1D1932;
`

const SearchF = styled.form`
    display:flex;
    align-items:center;
`

const SearchI = styled.input`
font-size:16px;
background-color:#1D1932;
border: 1px solid #1D1932;
`

function Search(){
    const {register, handleSubmit} = useForm();
    const onValue = (data) => {
    }

    return(
        <SearchW>
            <SearchF onSubmit={handleSubmit(onValue)}>
                <FaSearch style={{color:'white',margin:'0px 20px'}}/>
                <SearchI {...register('keyword',{required:true})}
                placeholder="Search Here"/>
            </SearchF>
        </SearchW>
    )
}

export default Search;