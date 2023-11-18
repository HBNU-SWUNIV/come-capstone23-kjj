import PropTypes from 'prop-types';
import Typography from '@mui/material/Typography';
import styled from 'styled-components';

function Title(props) {
  return (
    <Typography component="h2" variant="h6" color="primary" gutterBottom>
      <Text>{props.children}</Text>
    </Typography>
  );
}

Title.propTypes = {
  children: PropTypes.node,
};

export default Title;

const Text = styled.span`
  font-size: 16px;
  font-weight: 600;
  color: ${(props) => props.theme.colors.dark};
`;
