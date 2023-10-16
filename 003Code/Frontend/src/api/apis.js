import axios from 'axios';
import { ManagerBaseApi } from '../auth/authConfig';

const holiday_service_key = `ziROfCzWMmrKIseBzkXs58HpS39GI%2FmxjSEmUeZbKwYuyxnSc2kILXCBXlRpPZ8iam5cqwZqtw6db7CnWG%2FQQQ%3D%3D`;
const holiday_base_api = `http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo?`;

// charts
export async function getReservation(config) {
  try {
    const response = await axios.get(`${ManagerBaseApi}/state/next-week/user`, config);
    return response.data;
  } catch (err) {
    console.error('getReservation_Error=', err);
  }
}

export async function getIngredientsInfo(config) {
  try {
    const response = await axios.get(`${ManagerBaseApi}/menu/food`, config);
    return response.data;
  } catch (err) {
    console.error('getIngredientsInfo_Error=', err);
  }
}

// menus
export async function getMenus(config) {
  try {
    const response = await axios.get(`${ManagerBaseApi}/menu`, config);
    return response.data;
  } catch (err) {
    console.error('getMenus_Error =', err);
  }
}

// dailymenu
export async function getTodayMenu(config) {
  try {
    const response = await axios.get(`${ManagerBaseApi}/menu/planner`, config);
    return response.data;
  } catch (err) {
    console.error('getTodayMenuError =', err);
  }
}

export async function getDailyMenu(config, year, month) {
  try {
    const response = await axios.get(`api/user/planner/${year}/${month}`, config);
    return response.data;
  } catch (err) {
    console.error('getDailyMenu_Error=', err);
  }
}

// dayoff
export async function getOffDay(config, year, month) {
  try {
    const response = await axios.get(`/api/user/store/off/${year}/${month}`, config);
    return response.data;
  } catch (err) {
    console.error('getOffday_Error=', err);
  }
}

export async function getHoliday(year, month) {
  try {
    const response = await axios.get(
      `${holiday_base_api}solYear=${year}&solMonth=${month}&ServiceKey=${holiday_service_key}`
    );
    return response.data.response.body.items.item;
  } catch (err) {
    console.error('getHoliday_Error=', err);
  }
}
