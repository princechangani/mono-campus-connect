import React, { useState, useEffect } from 'react';
import {
    Box,
    Container,
    Grid,
    Paper,
    TextField,
    Button,
    Typography,
    Alert,
    Select,
    MenuItem,
    LoadingButton,
    Stack,
} from '@mui/material';
import CourseCard from '../components/CourseCard.jsx';
import CourseForm from '../components/CourseForm.jsx';
import { courseService } from '../services/courseService';
import { useAuth } from '../context/AuthContext.jsx';

const CoursesPage = () => {
    const [courses, setCourses] = useState([]);
    const [departmentFilter, setDepartmentFilter] = useState('');
    const [semesterFilter, setSemesterFilter] = useState('');
    const [openForm, setOpenForm] = useState(false);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const { isAuthenticated, role } = useAuth();

    useEffect(() => {
        loadCourses();
    }, [departmentFilter, semesterFilter]);

    const loadCourses = async () => {
        setLoading(true);
        setError('');
        try {
            if (departmentFilter) {
                const data = await courseService.getByDepartment(departmentFilter);
                setCourses(data);
            } else if (semesterFilter) {
                const data = await courseService.getBySemester(semesterFilter);
                setCourses(data);
            } else {
                const data = await courseService.getAll();
                setCourses(data);
            }
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleAddCourse = () => {
        if (role === 'ADMIN') {
            setSelectedCourse(null);
            setOpenForm(true);
        } else {
            setError('Only admins can add courses');
        }
    };

    const handleEditCourse = (course) => {
        if (role === 'ADMIN') {
            setSelectedCourse(course);
            setOpenForm(true);
        } else {
            setError('Only admins can edit courses');
        }
    };

    const handleDeleteCourse = async (id) => {
        if (role !== 'ADMIN') {
            setError('Only admins can delete courses');
            return;
        }

        if (window.confirm('Are you sure you want to delete this course?')) {
            try {
                await courseService.delete(id);
                loadCourses();
            } catch (error) {
                console.error('Error deleting course:', error);
            }
        }
    };

    const handleSubmitCourse = async (courseData) => {
        try {
            if (selectedCourse) {
                await courseService.update(selectedCourse.id, courseData);
            } else {
                await courseService.create(courseData);
            }
            loadCourses();
        } catch (error) {
            console.error('Error saving course:', error);
        }
    };

    if (!isAuthenticated) {
        return <div>You must be logged in to view this page</div>;
    }

    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Paper sx={{ p: 3 }}>
                <Stack spacing={3}>
                    <Typography variant="h4" component="h1" align="center">
                        Courses
                    </Typography>

                    {error && (
                        <Alert severity="error" sx={{ mt: 2 }}>
                            {error}
                        </Alert>
                    )}

                    <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                        <Select
                            label="Department"
                            value={departmentFilter}
                            onChange={(e) => setDepartmentFilter(e.target.value)}
                            sx={{ minWidth: 200 }}
                        >
                            <MenuItem value="">All Departments</MenuItem>
                            <MenuItem value="CSE">Computer Science</MenuItem>
                            <MenuItem value="ECE">Electronics & Communication</MenuItem>
                            <MenuItem value="ME">Mechanical</MenuItem>
                            <MenuItem value="CE">Civil</MenuItem>
                        </Select>

                        <Select
                            label="Semester"
                            value={semesterFilter}
                            onChange={(e) => setSemesterFilter(e.target.value)}
                            sx={{ minWidth: 200 }}
                        >
                            <MenuItem value="">All Semesters</MenuItem>
                            <MenuItem value="1">Semester 1</MenuItem>
                            <MenuItem value="2">Semester 2</MenuItem>
                            <MenuItem value="3">Semester 3</MenuItem>
                            <MenuItem value="4">Semester 4</MenuItem>
                            <MenuItem value="5">Semester 5</MenuItem>
                            <MenuItem value="6">Semester 6</MenuItem>
                            <MenuItem value="7">Semester 7</MenuItem>
                            <MenuItem value="8">Semester 8</MenuItem>
                        </Select>

                        {role === 'ADMIN' && (
                            <LoadingButton
                                variant="contained"
                                color="primary"
                                onClick={handleAddCourse}
                                loading={loading}
                            >
                                Add Course
                            </LoadingButton>
                        )}
                    </Box>

                    <Grid container spacing={3}>
                        {courses.map((course) => (
                            <Grid item xs={12} sm={6} md={4} key={course.id}>
                                <CourseCard
                                    course={course}
                                    onEdit={handleEditCourse}
                                    onDelete={handleDeleteCourse}
                                />
                            </Grid>
                        ))}
                    </Grid>
                </Stack>
            </Paper>

            <CourseForm
                open={openForm}
                onClose={() => setOpenForm(false)}
                course={selectedCourse}
                onSubmit={async (course) => {
                    try {
                        setLoading(true);
                        if (selectedCourse) {
                            await courseService.update(selectedCourse.id, course);
                        } else {
                            await courseService.create(course);
                        }
                        loadCourses();
                        setOpenForm(false);
                    } catch (error) {
                        setError(error.message);
                    } finally {
                        setLoading(false);
                    }
                }}
            />
        </Container>
    );
};

export default CoursesPage;
