import axios from 'axios';
import { ManagerBaseApi } from '../auth/authConfig';

// menus_pages
export async function getMenus(config) {
  try {
    const response = await axios.get(`${ManagerBaseApi}/menu`, config);
    return response.data;
  } catch (err) {
    console.error('getMenus_Error =', err);
  }
}
