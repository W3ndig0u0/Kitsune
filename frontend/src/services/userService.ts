import api from "./api";

export const userService = {
    login: (credentials: any) => api.post("/api/user/login", credentials),
    register: (userData: any) => api.post("/api/user/register", userData),
    getProfile: () => api.get("/api/user/profile"),
    updateProfile: (id: string, data: any) => api.put(`/api/user/update/${id}`, data)
};